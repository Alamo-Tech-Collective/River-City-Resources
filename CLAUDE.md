# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

River City Resources is a centralized directory for San Antonio disability resources built with Grails 6.2.3. The application provides a searchable, accessible directory of resources for individuals with disabilities, families, caregivers, and service providers.

## Development Commands

### Running the Application
```bash
./gradlew bootRun       # Start the application (runs on http://localhost:8080)
./gradlew -Dgrails.env=dev bootRun  # Explicitly run in development mode
```

### Testing
```bash
./gradlew test          # Run unit tests
./gradlew integrationTest  # Run integration tests
./gradlew test integrationTest  # Run all tests
./gradlew test --tests "com.example.SomeSpec"  # Run a specific test
```

### Building and Deployment
```bash
./gradlew build         # Build the application
./gradlew war          # Create WAR file for deployment
./gradlew clean        # Clean build artifacts
```

### Code Generation
```bash
./grailsw create-domain-class [ClassName]     # Create domain class
./grailsw create-controller [ControllerName]  # Create controller
./grailsw create-service [ServiceName]        # Create service
./grailsw generate-all [DomainClassName]      # Generate scaffolded views/controller
```

## Architecture Overview

### Framework Structure
This is a Grails 6.2.3 application following the convention-over-configuration MVC pattern:

- **Domain Classes** (`grails-app/domain/`) - GORM entities representing database tables
- **Controllers** (`grails-app/controllers/`) - Handle HTTP requests and responses
- **Services** (`grails-app/services/`) - Business logic layer, transactional by default
- **Views** (`grails-app/views/`) - GSP templates for rendering HTML
- **Assets** (`grails-app/assets/`) - JavaScript, CSS, and images managed by Asset Pipeline

### Key Design Patterns

1. **GORM (Grails Object Relational Mapping)**: Domain classes automatically get CRUD operations, validation, and query methods
2. **Service Layer**: All business logic should be in services (transactional by default)
3. **Command Objects**: Use for complex form validation
4. **Interceptors**: For cross-cutting concerns like authentication/authorization

### Database Configuration
- Development/Test: H2 in-memory database (auto-creates schema)
- Production: H2 file-based (configured in `application.yml`)
- GORM handles schema creation/updates automatically in development

### Testing Strategy
- **Unit Tests**: Use Spock specifications in `src/test/groovy/`
- **Integration Tests**: Use Geb/Selenium in `src/integration-test/groovy/`
- Test data builders recommended for complex domain object creation

## Project-Specific Implementation Notes

### Accessibility Requirements
The project requires WCAG 2.1 AA compliance. Key considerations:
- All views must support keyboard navigation
- Use semantic HTML elements
- Include proper ARIA labels
- Ensure color contrast ratios meet standards
- Test with screen readers

### Resource Directory Structure
Resources should be organized with these main categories:
- Transportation
- Employment  
- Education/Training
- Financial Support
- Support/Community
- Healthcare (future)
- Housing (future)
- Legal Services (future)

### Domain Model Considerations
When implementing the Resource domain class, include:
- Organization name, description, contact info
- Services offered (consider using HasMany relationship)
- Eligibility requirements (could be a separate domain)
- Hours of operation (consider custom type)
- Last updated tracking

### Search Implementation
Use GORM's criteria queries or HQL for complex searches:
- Full-text search across multiple fields
- Filter by category and eligibility
- Consider adding database indexes for performance

### Admin Interface
Leverage Grails scaffolding for rapid development:
- Use `generate-all` for basic CRUD operations
- Customize generated views for better UX
- Add Spring Security for authentication

### Asset Pipeline Usage
- Place custom CSS in `grails-app/assets/stylesheets/`
- JavaScript in `grails-app/assets/javascripts/`
- Use asset tags in GSP: `<asset:stylesheet>`, `<asset:javascript>`
- Assets are automatically minified in production

### Common Grails Patterns
- Use `@Transactional` annotation for service methods that modify data
- Leverage data binding with command objects
- Use Grails constraints for validation
- Implement custom validators when needed
- Use interceptors for authentication checks

### Performance Considerations
- Enable Hibernate second-level cache for read-heavy data
- Use pagination for large result sets
- Consider eager vs lazy loading for associations
- Profile database queries in development