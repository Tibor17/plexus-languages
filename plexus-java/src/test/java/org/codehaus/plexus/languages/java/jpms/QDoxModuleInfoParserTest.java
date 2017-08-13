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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.JavaExports;
import org.junit.Test;

public class QDoxModuleInfoParserTest
{
    private QDoxModuleInfoParser parser = new QDoxModuleInfoParser();

    @Test
    public void test() throws Exception
    {
        JavaModuleDescriptor moduleDescriptor = parser.fromSourcePath( new File( "src/test/resources/src.dir" ) );
        assertEquals( "a.b.c", moduleDescriptor.name() ); 
        assertEquals( "d.e", moduleDescriptor.requires().iterator().next().name() );
        
        Iterator<JavaExports> exportsIter = moduleDescriptor.exports().iterator();
        
        JavaExports exports = exportsIter.next(); 
        assertEquals( "f.g", exports.source() );
        
        exports = exportsIter.next(); 
        assertEquals( "f.g.h", exports.source() );
        assertEquals( new HashSet<>( Arrays.asList( "i.j", "k.l.m" ) ), exports.targets() );
        
    }

}
