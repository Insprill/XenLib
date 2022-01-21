[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![GPLv3 License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
<h1 align="center">XenLib</h1>
  <p align="center">
    XenLib is a simple library developed to use in my Spigot plugins.
    <br />
    <a href="https://insprill.net/javadocs/xenlib"><strong>View Javadocs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Insprill/Custom-Join-Messages">View Demo</a>
    ·
    <a href="https://github.com/Insprill/XenLib/issues">Report Bugs</a>
    ·
    <a href="https://github.com/Insprill/XenLib/issues">Request Features</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#compiling">Compiling</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- USAGE EXAMPLES -->
## Usage

### Implementing in your project
[![Release](https://jitpack.io/v/Insprill/XenLib.svg)](https://jitpack.io/#Insprill/XenLib)
#### Maven
```xml
    <repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
```
```xml
    <dependency>
        <groupId>net.insprill</groupId>
        <artifactId>XenLib</artifactId>
        <version>version</version>
        <scope>provided</scope>
    </dependency>
```
#### Gradle
```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}
```
```groovy
dependencies {
    implementation group: 'net.insprill', name: 'XenLib', version: 'version'
}
```
All versions can be found [here](https://jitpack.io/#Insprill/XenLib). 

### Using the library
In your plugin's onEnable, call `new XenLib(this);` to initialize the API.  
After that, all XenLib classes can be used.

Once it's time for you to compile your plugin, you'll need to shade it into your final jar as XenLib doesn't operate as a stand-alone plugin.  
To shade it with Maven, follow the official Maven Shade Plugin's [usage instructions](https://maven.apache.org/plugins/maven-shade-plugin/usage.html).  
To shade it with Gradle, you'll need to use the Shadow plugin. Usage instructions can be found [here](https://imperceptiblethoughts.com/shadow/introduction/).  

### Compiling

To compile XenLib, you need JDK 8 or higher and an internet connection.  
Clone this repo, then run `./gradlew shadowJar` from your terminal.  
You can find the compiled jar in the `build/libs` directory.  
If you wish to install it to your local Maven repository, run `gradlew publishToMavenLocal` after compiling.

_For more examples, please refer to the [Documentation](https://github.com/Insprill/XenLib/wiki)_.


<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/Insprill/XenLib/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the GNU GPLv3 License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Twitter - [@InsprillO_O](https://twitter.com/InsprillO_O)

Discord - [https://discord.gg/ZW4dvfr](https://discord.gg/SH7VyYtuC2)

Project Link: [https://github.com/Insprill/XenLib](https://github.com/Insprill/XenLib)





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/Insprill/XenLib.svg?style=for-the-badge
[contributors-url]: https://github.com/Insprill/XenLib/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Insprill/XenLib.svg?style=for-the-badge
[forks-url]: https://github.com/Insprill/XenLib/network/members
[stars-shield]: https://img.shields.io/github/stars/Insprill/XenLib.svg?style=for-the-badge
[stars-url]: https://github.com/Insprill/XenLib/stargazers
[issues-shield]: https://img.shields.io/github/issues/Insprill/XenLib.svg?style=for-the-badge
[issues-url]: https://github.com/Insprill/XenLib/issues
[license-shield]: https://img.shields.io/github/license/Insprill/XenLib.svg?style=for-the-badge
[license-url]: https://github.com/Insprill/XenLib/blob/master/LICENSE
