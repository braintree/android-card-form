require 'rake'
require 'io/console'

task :default => :tests

desc "Run Android lint"
task :lint do
  sh "./gradlew clean lint"
end

desc "Run Android unit tests and tests on a device or emulator"
task :tests => [:unit_tests, :integration_tests]

desc "Run Android unit tests"
task :unit_tests => :lint do
  sh "./gradlew --continue test"
end

desc "Run Android tests on a device or emulator"
task :integration_tests do
  output = `adb devices`
  if output.match(/device$/)
      sh "./gradlew --continue connectedAndroidTest"
  else
    puts "Please connect a device or start an emulator and try again"
    exit 1
  end
end

desc "Interactive release to publish new version"
task :release => :unit_tests do
  puts "Ensure unit tests build above was successful."
  puts "Ensure integration tests are passing by executing `rake integration_tests`."
  $stdin.gets

  puts "What version are you releasing? (x.x.x format)"
  version = $stdin.gets.chomp

  update_version(version)
  update_readme_version(version)

  prompt_for_sonatype_username_and_password

  sh "./gradlew clean :CardForm:uploadArchives"
  sh "./gradlew :CardForm:closeRepository"
  puts "Sleeping for one minute to allow CardForm module to close"
  sleep 60
  sh "./gradlew :CardForm:promoteRepository"

  post_release(version)
end

def prompt_for_sonatype_username_and_password
  puts "Enter Sonatype username:"
  ENV["SONATYPE_USERNAME"] = $stdin.gets.chomp

  puts "Enter Sonatype password:"
  ENV["SONATYPE_PASSWORD"] = $stdin.noecho(&:gets).chomp
end

def post_release(version)
  puts "\nArchives are uploaded! Committing and tagging #{version} and preparing for the next development iteration"
  sh "git commit -am 'Release #{version}'"
  sh "git tag #{version}"

  version_values = version.split('.')
  version_values[2] = version_values[2].to_i + 1
  update_version("#{version_values.join('.')}-SNAPSHOT")
  update_readme_snapshot_version(version_values.join('.'))
  increment_version_code
  sh "git commit -am 'Prepare for development'"

  puts "\nDone. Commits and tags have been created. If everything appears to be in order, hit ENTER to push."
  $stdin.gets

  sh "git push origin master #{version}"

  puts "\nUpdate the releases tab on GitHub and send a release notification email to braintree-sdk-announce@googlegroups.com. Press ENTER when done."
  $stdin.gets
end

def get_current_version
  current_version = nil
  File.foreach("CardForm/build.gradle") do |line|
    if match = line.match(/version '(\d+\.\d+\.\d+(-SNAPSHOT)?)'/)
      current_version = match.captures
    end
  end

  return current_version[0]
end

def increment_version_code
  new_build_file = ""
  File.foreach("CardForm/build.gradle") do |line|
    if line.match(/versionCode (\d+)/)
      new_build_file += line.gsub(/versionCode \d+/, "versionCode #{$1.to_i + 1}")
    else
      new_build_file += line
    end
  end
  IO.write('CardForm/build.gradle', new_build_file)
end

def update_version(version)
  IO.write("CardForm/build.gradle",
    File.open("CardForm/build.gradle") do |file|
      file.read.gsub(/version '\d+\.\d+\.\d+(-SNAPSHOT)?'/, "version '#{version}'")
    end
  )
end

def update_readme_version(version)
  IO.write("README.md",
    File.open("README.md") do |file|
      file.read.gsub(/:card-form:\d+\.\d+\.\d+'/, ":card-form:#{version}'")
    end
  )
end

def update_readme_snapshot_version(snapshot_version)
  IO.write("README.md",
    File.open("README.md") do |file|
      file.read.gsub(/:card-form:\d+\.\d+\.\d+-SNAPSHOT'/, ":card-form:#{snapshot_version}-SNAPSHOT'")
    end
  )
end

