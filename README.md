# libcbe

Conditional Block Entities. 

## Usage
Add the repository
```gradle
 repositories {
     maven {
         url = 'https://dl.bintray.com/boogiemonster1o1/cool-mods/'
     }
 }
 ```

Add the dependency 
```gradle
dependencies {
    modImplementation("io.github.boogiemonster1o1:LibCBE:${project.libcbe_version}")
    include("io.github.boogiemonster1o1:LibCBE:${project.libcbe_version}") // Includes LibCBE as a Jar-in-Jar embedded dependency
}
```


Add the property to `gradle.properties`. Skip this step if you decide to put the version in the dependency. 
```properties
libcbe_version = <the latest version>
```
[ ![Download](https://api.bintray.com/packages/boogiemonster1o1/cool-mods/LibCBE/images/download.svg) ](https://bintray.com/boogiemonster1o1/cool-mods/LibCBE/_latestVersion)
Find all versions [here](https://bintray.com/beta/#/boogiemonster1o1/cool-mods/LibCBE?tab=overview)

## License
This mod is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
