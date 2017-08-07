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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaModuleDescriptor;

/**
 * Extract information from module with QDox
 * 
 * @author Robert Scholte
 * @since 1.0.0
 */
public class QDoxModuleInfoParser
{

    public org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor fromSourcePath( Path path )
                    throws IOException
    {
        return fromSourcePath( path.toFile() );
    }
                    
    public org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor fromSourcePath( File path )
        throws IOException
    {
        File moduleDescriptor = new File( path, "module-info.java" );

        org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.Builder builder;
        if ( moduleDescriptor.exists() )
        {
            JavaModuleDescriptor descriptor = new JavaProjectBuilder().addSourceFolder( path ).getDescriptor();

            builder = org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.newModule( descriptor.getName() );
            
            for ( JavaModuleDescriptor.JavaRequires requires : descriptor.getRequires() )
            {
                builder.requires( requires.getModule().getName() );
            }
            
            for ( JavaModuleDescriptor.JavaExports exports : descriptor.getExports() )
            {
                builder.exports( exports.getSource().getName() );
            }
        }
        else
        {
            builder = org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.newAutomaticModule( null );
        }

        return builder.build();
    }

}
