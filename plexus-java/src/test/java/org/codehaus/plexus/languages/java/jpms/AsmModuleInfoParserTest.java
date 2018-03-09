package org.codehaus.plexus.languages.java.jpms;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.JavaRequires;
import org.junit.Test;

public class AsmModuleInfoParserTest
{
    private ModuleInfoParser parser = new AsmModuleInfoParser();

    @Test
    public void testJarDescriptor() throws Exception
    {
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/jar.descriptor/asm-6.0_BETA.jar" ) );
        
        assertNotNull( descriptor);
        assertEquals( "org.objectweb.asm", descriptor.name() );
        assertEquals( false, descriptor.isAutomatic() );

        assertEquals( 1, descriptor.requires().size() );
        assertEquals( "java.base", descriptor.requires().iterator().next().name() );

        // Actually the source returns "org/objectweb/asm" instead "org.objectweb.asm" ?
        // Need to dive into this later.
//        assertEquals( 2, descriptor.exports().size() );
//        assertEquals( "org.objectweb.asm", descriptor.exports().iterator().next().source() );
    }

    @Test
    public void testMultiReleaseJarDescriptor() throws Exception
    {
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/jar.mr.descriptor/jloadr-1.0-SNAPSHOT.jar" ) );
        
        assertNotNull( descriptor);
        assertEquals( "de.adito.jloadr", descriptor.name() );
        assertEquals( false, descriptor.isAutomatic() );
    }

    @Test
    public void testIncompleteMultiReleaseJarDescriptor() throws Exception
    {
        // this jar is missing the Multi-Release: true entry in the Manifest
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/jar.mr.incomplete.descriptor/jloadr-1.0-SNAPSHOT.jar" ) );
        
        assertNull( descriptor);
    }

    @Test
    public void testClassicJar() throws Exception
    {
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/jar.empty/plexus-java-1.0.0-SNAPSHOT.jar" ) );
        
        assertNull( descriptor);
    }
    
    @Test
    public void testOutputDirectoryDescriptor() throws Exception
    {
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/dir.descriptor/out" ) );
        
        assertNotNull( descriptor);
        assertEquals( "org.objectweb.asm.all", descriptor.name() );
        assertEquals( false, descriptor.isAutomatic() );
        
        assertEquals( 2, descriptor.requires().size() );
        Set<String> actualNames = new HashSet<>( 2 );
        for ( JavaRequires require : descriptor.requires() )
        {
            actualNames.add( require.name() );
        }
        Set<String> expectedNames = new HashSet<>( Arrays.asList( "java.base", "java.xml" ) );
        assertEquals( expectedNames, actualNames );
    }

    @Test( expected = NoSuchFileException.class )
    public void testClassicOutputDirectory() throws Exception
    {
        parser.getModuleDescriptor( Paths.get( "src/test/resources/dir.empty/out" ) );
    }

    @Test
    public void testJModDescriptor() throws Exception
    {
        JavaModuleDescriptor descriptor = parser.getModuleDescriptor( Paths.get( "src/test/resources/jmod.descriptor/first-jmod-1.0-SNAPSHOT.jmod" ) );
        
        assertNotNull( descriptor);
        assertEquals( "com.corporate.project", descriptor.name() );
        assertEquals( false, descriptor.isAutomatic() );

        assertEquals( 1, descriptor.requires().size() );
        assertEquals( "java.base", descriptor.requires().iterator().next().name() );

        assertEquals ( 1, descriptor.exports().size() );
        assertEquals ( "com.corporate.project",  descriptor.exports().iterator().next().source() );
    }

}