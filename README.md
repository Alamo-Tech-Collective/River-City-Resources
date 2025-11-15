## River City Resources

Find disability resources in San Antonio, TX. Browse our comprehensive directory of transportation, employment, education, financial support, and community services for people with disabilities.


## Install Project Dependencies

* **Docker Desktop:** https://docs.docker.com/desktop/setup/install/mac-install/

* **SDKMAN:** https://sdkman.io/install/

* **JDK:** `sdk install java 11.0.24-tem`

* **GRAILS:** `sdk install grails`

## Start project

Refer to [Docker README.md](/docker/README.md)

## Hot Reloading (Development)

The project is configured with Spring Boot DevTools for hot reloading during development. This means you don't need to restart the application when making changes to:

- **GSP views** (`grails-app/views/`) - Changes appear immediately
- **Controllers** (`grails-app/controllers/`) - Auto-restart on save
- **Services** (`grails-app/services/`) - Auto-restart on save
- **Domain classes** (`grails-app/domain/`) - Auto-restart on save
- **Configuration** (`grails-app/conf/`) - Auto-restart on save

### How it works

1. Start the application with `./gradlew bootRun`
2. Make changes to any of the files above
3. Save the file - the application will automatically restart (you'll see a message in the console)
4. Refresh your browser to see the changes

**Note:** The first restart after starting the app may take a few seconds. Subsequent restarts are typically faster.

### Browser LiveReload (Optional)

If you want automatic browser refresh when files change, you can install a LiveReload browser extension:
- Chrome: [LiveReload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei)
- Firefox: [LiveReload](https://addons.mozilla.org/en-US/firefox/addon/livereload-web-extension/)

The LiveReload server runs on port `35729` by default.

## Troubleshooting GitHub

To troubleshoot GitHub related issues you can read the [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

## How to Contribute Code

To contribute you will need to fork the repo on a feature branch, and create a pull request. This process is outlined in [CONTRIBUTING.md](CONTRIBUTING.md)

## Contributors

We're incredibly grateful for everyone who has helped make this project better! Your contributions, big or small, are truly valued.

### How Get Involved

Want to see your name here? We welcome all kinds of contributions, including:

* **Code:** Bug fixes, new features, improvements.
* **Documentation:** Enhancements, corrections, new guides.
* **Testing:** Finding bugs, providing feedback.

If you've contributed and don't see your name, or if you'd like to update your information, please open an issue or pull request, and we'll gladly add you!

### Our Amazing Contributors

* **[Brandon](https://github.com/TheRetroRoot)** - Initial project setup, project deployment, SEO, accessibility, and resources research.
* **[Paul Thompson](https://github.com/portalman32)** - Initial project idea and initial logo design.

## Grails 6.2.3 Documentation
- [User Guide](https://docs.grails.org/6.2.3/guide/index.html)
- [API Reference](https://docs.grails.org/6.2.3/api/index.html)
- [Grails Guides](https://guides.grails.org/index.html)
---

## Feature scaffolding documentation

- [Grails Scaffolding Plugin documentation](https://grails.github.io/scaffolding/latest/groovydoc/)

- [https://grails-fields-plugin.github.io/grails-fields/latest/guide/index.html](https://grails-fields-plugin.github.io/grails-fields/latest/guide/index.html)

## Feature geb documentation

- [Grails Geb Functional Testing for Grails documentation](https://github.com/grails3-plugins/geb#readme)

- [https://www.gebish.org/manual/current/](https://www.gebish.org/manual/current/)

## Feature asset-pipeline-grails documentation

- [Grails Asset Pipeline Core documentation](https://www.asset-pipeline.com/manual/)