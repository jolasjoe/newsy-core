Pod::Spec.new do |spec|
    spec.name                     = 'newsy-core'
    spec.version                  = '0.0.1-alpha-2'
    spec.homepage                 = 'https://github.com/jolasjoe/newsy-core'
    spec.source                   = { :git => "https://github.com/jolasjoe/newsy-core.git", :tag => "#{spec.version}" }
    spec.authors                  = 'Jolas'
    spec.license                  = { :type => "MIT", :text=> <<-LICENSE
                                  MIT License

                                  Copyright (c) 2018 Zoho Corporation

                                  Permission is hereby granted, free of charge, to any person obtaining a copy
                                  of this software and associated documentation files (the "Software"), to deal
                                  in the Software without restriction, including without limitation the rights
                                  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
                                  copies of the Software, and to permit persons to whom the Software is
                                  furnished to do so, subject to the following conditions:

                                  The above copyright notice and this permission notice shall be included in all
                                  copies or substantial portions of the Software.

                                  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
                                  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
                                  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
                                  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
                                  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
                                  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
                                  SOFTWARE
                                  LICENSE
                                  }
    spec.summary                  = 'newsy-core Kotlin/Native module'

    spec.static_framework         = true
    spec.vendored_frameworks      = "build/cocoapods/framework/newsycore.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"

            

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=watchsimulator*]' => 'watchos_x86',
        'KOTLIN_TARGET[sdk=watchos*]' => 'watchos_arm',
        'KOTLIN_TARGET[sdk=appletvsimulator*]' => 'tvos_x64',
        'KOTLIN_TARGET[sdk=appletvos*]' => 'tvos_arm64',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    spec.script_phases = [
        {
            :name => 'Build newsy_core',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/gradlew" -p "$REPO_ROOT" ::syncFramework \
                    -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
                    -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \
                    -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
                    -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
            SCRIPT
        }
    ]
end