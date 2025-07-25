package riverCityResources

import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        createInitialSecurityRoles()
        createInitialAdminUser()
        createInitialCategories()
        createComprehensiveTestData()
        // Database indexes would be created in production via database migrations
        // For now, we'll skip them in development
    }
    
    def destroy = {
    }
    
    @Transactional
    void createInitialSecurityRoles() {
        if (Role.count() == 0) {
            def adminRole = new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
            def userRole = new Role(authority: 'ROLE_USER').save(failOnError: true)
            
            log.info "Created initial security roles"
        }
    }
    
    @Transactional
    void createInitialAdminUser() {
        if (User.count() == 0) {
            def adminRole = Role.findByAuthority('ROLE_ADMIN')
            
            def adminUser = new User(
                username: 'admin',
                password: 'admin123', // This will be encoded by Spring Security
                email: 'admin@rivercityresources.org',
                firstName: 'Admin',
                lastName: 'User',
                enabled: true
            ).save(failOnError: true)
            
            UserRole.create(adminUser, adminRole, true)
            
            log.info "Created initial admin user (username: admin, password: admin123)"
        }
    }
    
    @Transactional
    void createInitialCategories() {
        if (Category.count() == 0) {
            def categories = [
                [name: 'Transportation', description: 'Transportation services and resources for individuals with disabilities', displayOrder: 1],
                [name: 'Employment', description: 'Job placement, training, and employment support services', displayOrder: 2],
                [name: 'Education/Training', description: 'Educational programs and vocational training opportunities', displayOrder: 3],
                [name: 'Financial Support', description: 'Financial assistance programs and benefits', displayOrder: 4],
                [name: 'Support/Community', description: 'Support groups, community programs, and social services', displayOrder: 5],
                [name: 'Healthcare', description: 'Healthcare services and medical resources (Coming Soon)', displayOrder: 6, active: false],
                [name: 'Housing', description: 'Housing assistance and accessibility modifications (Coming Soon)', displayOrder: 7, active: false],
                [name: 'Legal Services', description: 'Legal aid and advocacy services (Coming Soon)', displayOrder: 8, active: false]
            ]
            
            categories.each { categoryData ->
                new Category(categoryData).save(failOnError: true)
            }
            
            log.info "Created ${Category.count()} initial categories"
        }
    }
    
    @Transactional
    void createComprehensiveTestData() {
        if (Resource.count() == 0) {
            def categories = [
                'Transportation': Category.findByName('Transportation'),
                'Employment': Category.findByName('Employment'),
                'Education/Training': Category.findByName('Education/Training'),
                'Financial Support': Category.findByName('Financial Support'),
                'Support/Community': Category.findByName('Support/Community')
            ]
            
            // Transportation Resources (4)
            createTransportationResources(categories['Transportation'])
            
            // Employment Resources (4)
            createEmploymentResources(categories['Employment'])
            
            // Education/Training Resources (4)
            createEducationResources(categories['Education/Training'])
            
            // Financial Support Resources (4)
            createFinancialSupportResources(categories['Financial Support'])
            
            // Support/Community Resources (4)
            createSupportCommunityResources(categories['Support/Community'])
            
            // Additional Disability Services (Added January 2025)
            createAdditionalDisabilityServices(categories)
            
            log.info "Created ${Resource.count()} comprehensive test resources"
        }
    }
    
    private void createTransportationResources(Category category) {
        // VIA Metropolitan Transit
        def viaResource = new Resource(
            name: 'VIA Metropolitan Transit',
            description: 'VIA offers accessible public transportation services including paratransit for individuals with disabilities who cannot use regular bus service. VIAtrans provides curb-to-curb service for eligible riders throughout Bexar County.',
            servicesOffered: '''• Paratransit service (VIAtrans) - door-to-door transportation
• Accessible bus routes with wheelchair lifts and ramps
• Reduced fare programs for seniors and people with disabilities
• Travel training programs to help learn the bus system
• Real-time bus tracking via mobile app
• Trip planning assistance for riders with disabilities''',
            hoursOfOperation: 'VIAtrans: Monday-Saturday 5:00 AM - 1:00 AM, Sunday 5:00 AM - 12:00 AM\nCustomer Service: Monday-Friday 6:00 AM - 10:00 PM, Saturday 8:00 AM - 5:00 PM',
            featured: true,
            category: category
        )
        
        viaResource.contact = new Contact(
            phone: '(210) 362-2020',
            email: 'customerservice@viainfo.net',
            address: '123 N Medina St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78207',
            website: 'https://www.viainfo.net'
        )
        
        viaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be unable to use regular fixed-route bus service due to disability',
            type: 'disability'
        ))
        viaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'ADA paratransit eligibility certification required',
            type: 'other'
        ))
        
        viaResource.save(failOnError: true)
        
        // Alamo Area Council of Governments (AACOG) Rural Transportation
        def aacogResource = new Resource(
            name: 'AACOG Rural Transportation Program',
            description: 'AACOG provides specialized transportation services for elderly and disabled residents in rural areas surrounding San Antonio. Services include medical appointments, shopping, and essential errands.',
            servicesOffered: '''• Medical appointment transportation
• Grocery shopping trips
• Transportation to senior centers
• Wheelchair accessible vehicles
• Door-to-door service in rural Bexar County
• Group transportation for senior programs''',
            hoursOfOperation: 'Monday-Friday 6:00 AM - 6:00 PM (Advance reservation required)',
            category: category
        )
        
        aacogResource.contact = new Contact(
            phone: '(210) 362-5200',
            email: 'ruraltransport@aacog.com',
            address: '8700 Tesoro Dr, Suite 700',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78217',
            website: 'https://www.aacog.com'
        )
        
        aacogResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be 65 years or older OR have a disability',
            type: 'age'
        ))
        aacogResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must live in rural areas of Bexar County',
            type: 'other'
        ))
        
        aacogResource.save(failOnError: true)
        
        // Yellow Cab Wheelchair Accessible Service
        def yellowCabResource = new Resource(
            name: 'Yellow Cab Wheelchair Accessible Service',
            description: 'Yellow Cab San Antonio operates wheelchair accessible taxi vehicles equipped with ramps and securements. Available for on-demand transportation throughout the San Antonio metro area.',
            servicesOffered: '''• Wheelchair accessible taxi service
• 24/7 availability
• Trained drivers for disability assistance
• Accepts Medicaid transportation vouchers
• Airport transportation
• Same fare rates as standard taxis''',
            hoursOfOperation: '24 hours a day, 7 days a week',
            category: category
        )
        
        yellowCabResource.contact = new Contact(
            phone: '(210) 222-2222',
            email: 'dispatch@yellowcabsa.com',
            address: '1250 S Brazos St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78207',
            website: 'https://www.yellowcabsanantonio.com'
        )
        
        yellowCabResource.save(failOnError: true)
        
        // SAMM Ministries Transportation
        def sammResource = new Resource(
            name: 'SAMM Ministries Transportation Services',
            description: 'SAMM Ministries provides free transportation for seniors and individuals with disabilities to medical appointments, grocery stores, and other essential destinations in the Medical Center area.',
            servicesOffered: '''• Free medical appointment transportation
• Grocery store trips
• Pharmacy runs
• Social service appointments
• Wheelchair accessible vans
• Volunteer driver program''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 4:00 PM (48-hour advance notice required)',
            category: category
        )
        
        sammResource.contact = new Contact(
            phone: '(210) 737-7266',
            email: 'transportation@sammministry.org',
            address: '5038 Wurzbach Rd',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78238',
            website: 'https://www.sammministry.org'
        )
        
        sammResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be 60 years or older OR have a disability',
            type: 'age'
        ))
        sammResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Income below 200% of federal poverty level',
            type: 'income'
        ))
        
        sammResource.save(failOnError: true)
    }
    
    private void createEmploymentResources(Category category) {
        // Goodwill San Antonio
        def goodwillResource = new Resource(
            name: 'Goodwill San Antonio',
            description: 'Goodwill provides comprehensive job training, employment placement services, and career development programs for people with disabilities and other barriers to employment. Their mission is to help individuals achieve economic independence.',
            servicesOffered: '''• Job readiness training and soft skills development
• Career counseling and vocational assessment
• Job placement assistance and employer connections
• Resume writing and interview preparation workshops
• Computer skills training
• On-the-job training programs with local employers
• Disability accommodation consultation''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM\nCareer Centers open until 7:00 PM on Tuesdays',
            featured: true,
            category: category
        )
        
        goodwillResource.contact = new Contact(
            phone: '(210) 924-8581',
            email: 'careers@goodwillsa.org',
            address: '406 W Commerce St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78207',
            website: 'https://www.goodwillsa.org'
        )
        
        goodwillResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Open to individuals with disabilities, veterans, and those facing employment barriers',
            type: 'other'
        ))
        
        goodwillResource.save(failOnError: true)
        
        // Texas Workforce Solutions Alamo
        def twsResource = new Resource(
            name: 'Texas Workforce Solutions Alamo - Disability Services',
            description: 'Texas Workforce Solutions Alamo offers specialized employment services for individuals with disabilities, including vocational rehabilitation, job coaching, and assistive technology assessments.',
            servicesOffered: '''• Vocational rehabilitation counseling
• Job search assistance and placement
• Assistive technology evaluation and training
• Workplace accommodation planning
• Skills training and certification programs
• Supported employment services
• Benefits counseling for disability recipients''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM',
            category: category
        )
        
        twsResource.contact = new Contact(
            phone: '(210) 272-3260',
            email: 'info@workforcesolutionsalamo.org',
            address: '3355 West Military Dr',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78211',
            website: 'https://www.workforcesolutionsalamo.org'
        )
        
        twsResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a documented disability that impacts employment',
            type: 'disability'
        ))
        twsResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be authorized to work in the United States',
            type: 'other'
        ))
        
        twsResource.save(failOnError: true)
        
        // Project SEARCH San Antonio
        def projectSearchResource = new Resource(
            name: 'Project SEARCH San Antonio',
            description: 'Project SEARCH is a business-led transition program for young adults with intellectual and developmental disabilities. Students complete internships at local hospitals and businesses while developing employment skills.',
            servicesOffered: '''• Year-long employment preparation program
• Three 10-week internship rotations
• Job coaching and mentoring
• Employment skills curriculum
• Career exploration and assessment
• Job placement assistance upon completion
• Partnership with local healthcare facilities''',
            hoursOfOperation: 'Academic Year: Monday-Friday 8:00 AM - 2:30 PM',
            category: category
        )
        
        projectSearchResource.contact = new Contact(
            phone: '(210) 207-7830',
            email: 'projectsearch@saisd.net',
            address: '141 Lavaca St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78210',
            website: 'https://www.saisd.net/projectsearch'
        )
        
        projectSearchResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be 18-22 years old',
            type: 'age'
        ))
        projectSearchResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have intellectual or developmental disability',
            type: 'disability'
        ))
        projectSearchResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have completed high school requirements',
            type: 'other'
        ))
        
        projectSearchResource.save(failOnError: true)
        
        // RISE Recovery
        def riseResource = new Resource(
            name: 'RISE Recovery Supported Employment',
            description: 'RISE Recovery offers supported employment services for individuals with mental health conditions and co-occurring disorders. Their evidence-based approach helps clients find and maintain competitive employment.',
            servicesOffered: '''• Individual Placement and Support (IPS) model
• Rapid job search and placement
• Ongoing job coaching and support
• Benefits counseling and planning
• Employer education on mental health
• Crisis intervention support
• Integration with mental health treatment''',
            hoursOfOperation: 'Monday-Friday 8:30 AM - 5:00 PM',
            category: category
        )
        
        riseResource.contact = new Contact(
            phone: '(210) 227-2634',
            email: 'employment@riserecovery.org',
            address: '2203 Babcock Rd',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78229',
            website: 'https://www.riserecovery.org'
        )
        
        riseResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a mental health diagnosis',
            type: 'disability'
        ))
        riseResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'age'
        ))
        
        riseResource.save(failOnError: true)
    }
    
    private void createEducationResources(Category category) {
        // Any Baby Can
        def anyBabyCanResource = new Resource(
            name: 'Any Baby Can',
            description: 'Any Baby Can provides early childhood intervention, family support services, and special education advocacy for children with disabilities and their families throughout San Antonio and South Texas.',
            servicesOffered: '''• Early Childhood Intervention (ECI) services
• Parent education and training programs
• Special education advocacy and IEP support
• Respite care services for families
• Case management and service coordination
• Developmental screenings and assessments
• Family support groups and counseling
• Assistive technology resources''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM\nEvening appointments available',
            featured: true,
            category: category
        )
        
        anyBabyCanResource.contact = new Contact(
            phone: '(210) 227-0170',
            email: 'info@anybabycansa.org',
            address: '217 Howard St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.anybabycansa.org'
        )
        
        anyBabyCanResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Children must be under 3 years old for ECI services',
            type: 'age'
        ))
        anyBabyCanResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a documented developmental delay or disability',
            type: 'disability'
        ))
        
        anyBabyCanResource.save(failOnError: true)
        
        // The Arc of San Antonio
        def arcResource = new Resource(
            name: 'The Arc of San Antonio',
            description: 'The Arc provides educational programs, life skills training, and advocacy services for individuals with intellectual and developmental disabilities across all ages.',
            servicesOffered: '''• Adult education and literacy programs
• Life skills and independent living training
• Self-advocacy training and leadership development
• Technology training and digital literacy
• Art and music therapy programs
• Social skills groups and workshops
• Transition planning for young adults''',
            hoursOfOperation: 'Monday-Friday 8:30 AM - 4:30 PM\nSaturday programs 9:00 AM - 2:00 PM',
            category: category
        )
        
        arcResource.contact = new Contact(
            phone: '(210) 490-4300',
            email: 'info@arc-sa.org',
            address: '13430 West Ave',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78216',
            website: 'https://www.arc-sa.org'
        )
        
        arcResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have intellectual or developmental disability',
            type: 'disability'
        ))
        
        arcResource.save(failOnError: true)
        
        // Learning Disabilities Association of America - San Antonio
        def ldaResource = new Resource(
            name: 'Learning Disabilities Association - San Antonio Chapter',
            description: 'LDA San Antonio provides educational support, tutoring, and advocacy for individuals with learning disabilities. They offer assessment referrals and educational planning assistance.',
            servicesOffered: '''• Educational assessments and referrals
• One-on-one tutoring services
• Study skills and organizational training
• Parent education workshops
• Teacher consultation services
• Assistive technology training
• College preparation programs''',
            hoursOfOperation: 'Monday-Thursday 9:00 AM - 6:00 PM\nFriday 9:00 AM - 3:00 PM',
            category: category
        )
        
        ldaResource.contact = new Contact(
            phone: '(210) 495-8252',
            email: 'info@ldasa.org',
            address: '7701 Broadway St, Suite 204',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78209',
            website: 'https://www.ldasa.org'
        )
        
        ldaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a diagnosed learning disability',
            type: 'disability'
        ))
        
        ldaResource.save(failOnError: true)
        
        // Palo Alto College Disability Support Services
        def pacResource = new Resource(
            name: 'Palo Alto College - Disability Support Services',
            description: 'Palo Alto College provides comprehensive support services for students with disabilities, including academic accommodations, assistive technology, and transition programs from high school to college.',
            servicesOffered: '''• Academic accommodations and modifications
• Assistive technology lab and training
• Note-taking assistance and services
• Alternative testing arrangements
• Sign language interpreting
• Priority registration for classes
• Transition programs for high school students
• Career counseling for students with disabilities''',
            hoursOfOperation: 'Monday-Thursday 8:00 AM - 7:00 PM\nFriday 8:00 AM - 4:30 PM',
            category: category
        )
        
        pacResource.contact = new Contact(
            phone: '(210) 486-3020',
            email: 'pac-dss@alamo.edu',
            address: '1400 W Villaret Blvd',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78224',
            website: 'https://www.alamo.edu/pac/disability-support'
        )
        
        pacResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be enrolled or accepted at Palo Alto College',
            type: 'other'
        ))
        pacResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must provide documentation of disability',
            type: 'disability'
        ))
        
        pacResource.save(failOnError: true)
    }
    
    private void createFinancialSupportResources(Category category) {
        // Social Security Administration
        def ssaResource = new Resource(
            name: 'Social Security Administration - San Antonio',
            description: 'The Social Security Administration provides disability benefits including Social Security Disability Insurance (SSDI) and Supplemental Security Income (SSI) for eligible individuals with disabilities.',
            servicesOffered: '''• Social Security Disability Insurance (SSDI) applications
• Supplemental Security Income (SSI) applications
• Benefits verification and management
• Work incentive programs (Ticket to Work)
• Medicare enrollment assistance
• Representative payee services
• Appeals and reconsideration assistance''',
            hoursOfOperation: 'Monday-Friday 9:00 AM - 4:00 PM\nPhone service: Monday-Friday 8:00 AM - 7:00 PM',
            featured: true,
            category: category
        )
        
        ssaResource.contact = new Contact(
            phone: '1-800-772-1213',
            email: 'info@ssa.gov',
            address: '811 NE Loop 410, Suite 300',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78209',
            website: 'https://www.ssa.gov'
        )
        
        ssaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a disability expected to last 12 months or result in death',
            type: 'disability'
        ))
        ssaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must meet work credit requirements for SSDI',
            type: 'other'
        ))
        ssaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must meet income and resource limits for SSI',
            type: 'income'
        ))
        
        ssaResource.save(failOnError: true)
        
        // United Way 2-1-1
        def unitedWayResource = new Resource(
            name: 'United Way 2-1-1 Helpline',
            description: 'United Way 2-1-1 is a free helpline that connects individuals with disabilities to financial assistance programs, utility payment help, food assistance, and other essential services.',
            servicesOffered: '''• 24/7 helpline in multiple languages
• Utility assistance program referrals
• Food pantry and SNAP benefit information
• Emergency financial assistance connections
• Prescription assistance programs
• Housing and rent assistance resources
• Free tax preparation services (seasonal)''',
            hoursOfOperation: '24 hours a day, 7 days a week\nLive assistance available',
            category: category
        )
        
        unitedWayResource.contact = new Contact(
            phone: '(210) 227-4357',
            email: 'contact@unitedwaysa.org',
            address: '700 S Alamo St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78205',
            website: 'https://www.211texas.org'
        )
        
        unitedWayResource.save(failOnError: true)
        
        // Family Service Association
        def fsaResource = new Resource(
            name: 'Family Service Association',
            description: 'Family Service provides emergency financial assistance, benefits counseling, and case management services for individuals with disabilities and their families facing financial hardship.',
            servicesOffered: '''• Emergency financial assistance for rent/utilities
• Benefits application assistance
• Financial literacy education
• Representative payee services
• Money management programs
• Crisis intervention services
• Case management and advocacy''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM\nEvening appointments available',
            category: category
        )
        
        fsaResource.contact = new Contact(
            phone: '(210) 299-2400',
            email: 'info@family-service.org',
            address: '702 San Pedro Ave',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.family-service.org'
        )
        
        fsaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must demonstrate financial need',
            type: 'income'
        ))
        fsaResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must reside in Bexar County',
            type: 'other'
        ))
        
        fsaResource.save(failOnError: true)
        
        // Disability Rights Texas
        def drtxResource = new Resource(
            name: 'Disability Rights Texas - San Antonio Office',
            description: 'Disability Rights Texas provides free legal advocacy and assistance with disability benefits appeals, discrimination cases, and access to public benefits and services.',
            servicesOffered: '''• Social Security disability appeals assistance
• Medicaid and Medicare advocacy
• Special education funding advocacy
• Employment discrimination cases
• Housing discrimination assistance
• Public benefits denial appeals
• Systemic advocacy for policy change''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM',
            category: category
        )
        
        drtxResource.contact = new Contact(
            phone: '(210) 737-0499',
            email: 'info@disabilityrightstx.org',
            address: '1305 San Pedro Ave, Suite 100',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.disabilityrightstx.org'
        )
        
        drtxResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a disability as defined by the ADA',
            type: 'disability'
        ))
        
        drtxResource.save(failOnError: true)
    }
    
    private void createSupportCommunityResources(Category category) {
        // Independence Hill Community Center
        def indHillResource = new Resource(
            name: 'Independence Hill Community Center',
            description: 'Independence Hill provides independent living services, peer support, and advocacy for individuals with disabilities. They offer a welcoming community space and comprehensive support programs.',
            servicesOffered: '''• Independent living skills training
• Peer support and mentoring programs
• Disability awareness and advocacy training
• Computer access and training lab
• Social and recreational activities
• Information and referral services
• Youth transition programs
• Assistive technology demonstrations''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM\nSaturday 10:00 AM - 2:00 PM',
            featured: true,
            category: category
        )
        
        indHillResource.contact = new Contact(
            phone: '(210) 734-2020',
            email: 'info@independencehill.org',
            address: '2202 Babcock Rd',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78229',
            website: 'https://www.independencehill.org'
        )
        
        indHillResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Open to all individuals with disabilities',
            type: 'disability'
        ))
        
        indHillResource.save(failOnError: true)
        
        // Morgan\'s Wonderland
        def morgansResource = new Resource(
            name: 'Morgan\'s Wonderland',
            description: 'Morgan\'s Wonderland is an inclusive theme park designed for individuals with special needs and their families. It provides a safe, accessible environment for recreation and socialization.',
            servicesOffered: '''• Fully accessible theme park attractions
• Wheelchair and adaptive equipment rentals
• Sensory-friendly environments
• Special events for disability community
• Summer camp programs
• Sports leagues and activities
• Birthday party accommodations
• Group visit programs''',
            hoursOfOperation: 'Seasonal hours - Check website\nTypically: Friday-Sunday 10:00 AM - 6:00 PM',
            category: category
        )
        
        morgansResource.contact = new Contact(
            phone: '(210) 495-5888',
            email: 'info@morganswonderland.com',
            address: '5223 David Edwards Dr',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78233',
            website: 'https://www.morganswonderland.com'
        )
        
        morgansResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Free admission for individuals with disabilities',
            type: 'disability'
        ))
        
        morgansResource.save(failOnError: true)
        
        // NAMI San Antonio
        def namiResource = new Resource(
            name: 'NAMI San Antonio (National Alliance on Mental Illness)',
            description: 'NAMI San Antonio provides support groups, education programs, and advocacy for individuals living with mental illness and their families.',
            servicesOffered: '''• Peer-led support groups
• Family support programs
• Mental health education classes
• Crisis intervention resources
• Advocacy and public awareness
• Connections to treatment services
• Suicide prevention programs
• Veterans mental health support''',
            hoursOfOperation: 'Monday-Friday 9:00 AM - 5:00 PM\nSupport groups: Various evening and weekend times',
            category: category
        )
        
        namiResource.contact = new Contact(
            phone: '(210) 734-3349',
            email: 'info@nami-sat.org',
            address: '510 Belknap Pl',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.nami-sat.org'
        )
        
        namiResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Open to individuals with mental health conditions and their families',
            type: 'disability'
        ))
        
        namiResource.save(failOnError: true)
        
        // Autism Community Network
        def autismResource = new Resource(
            name: 'Autism Community Network',
            description: 'ACN provides comprehensive support services, social programs, and family resources for individuals on the autism spectrum throughout their lifespan.',
            servicesOffered: '''• Social skills groups for all ages
• Parent support and training
• Respite care services
• Summer camp and after-school programs
• Adult day programs
• Job readiness training
• Sensory-friendly events
• Behavior support consultation''',
            hoursOfOperation: 'Monday-Friday 8:30 AM - 5:30 PM\nWeekend programs available',
            category: category
        )
        
        autismResource.contact = new Contact(
            phone: '(210) 558-2708',
            email: 'info@autismcommunitynetwork.org',
            address: '14507 IH-35 North',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78232',
            website: 'https://www.autismcommunitynetwork.org'
        )
        
        autismResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have autism spectrum disorder diagnosis',
            type: 'disability'
        ))
        
        autismResource.save(failOnError: true)
    }
    
    // Database indexes would be created in production via database migrations
    // Examples of useful indexes for this application:
    // - idx_resource_name ON resource(name)
    // - idx_resource_category ON resource(category_id)
    // - idx_resource_active ON resource(active)
    // - idx_resource_featured ON resource(featured)
    // - idx_resource_active_category ON resource(active, category_id)
    // - Full-text search indexes on name, description, services_offered fields
    
    private void createAdditionalDisabilityServices(Map<String, Category> categories) {
        // DisabilitySA - Support/Community
        def disabilitySAResource = new Resource(
            name: 'DisabilitySA',
            description: 'DisabilitySA provides programs, resources, and volunteer opportunities to improve the lives of people with disabilities in San Antonio. They work closely with the City of San Antonio Disability Access Office and Bexar County TACPD.',
            servicesOffered: '''• Community advocacy and awareness programs
• Volunteer opportunities for and with people with disabilities
• Resource navigation and referral services
• Disability awareness training for businesses
• Community events and inclusive activities
• Partnership programs with local organizations
• Educational workshops and seminars
• Support for disability rights initiatives''',
            hoursOfOperation: 'Monday-Friday 9:00 AM - 5:00 PM',
            category: categories['Support/Community']
        )
        
        disabilitySAResource.contact = new Contact(
            phone: '(210) 477-7000',
            email: 'info@disabilitysa.org',
            address: '1222 N Main Ave, Suite 700',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.disabilitysa.org'
        )
        
        disabilitySAResource.save(failOnError: true)
        
        // SAILS - San Antonio Independent Living Services - Support/Community
        def sailsResource = new Resource(
            name: 'San Antonio Independent Living Services (SAILS)',
            description: 'SAILS has served people with disabilities since 1981 as a federally and state-designated center for independent living. They provide comprehensive services to help individuals with disabilities live independently in the community.',
            servicesOffered: '''• Independent living skills training
• Benefits assistance and social security planning
• Deaf support services and telecommunications assistance
• Adaptive telecommunication device training
• Relocation services from nursing facilities to community
• Peer support and mentoring programs
• Information and referral services
• Recreation and social events for persons with disabilities
• Rehabilitation technology through Outsourcing program
• Assistive technology including ramps, wheelchairs, and vehicle modifications''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM',
            featured: true,
            category: categories['Support/Community']
        )
        
        sailsResource.contact = new Contact(
            phone: '(210) 281-1878',
            email: 'info@sailstx.org',
            address: '1028 S Alamo St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78210',
            website: 'https://www.sailstx.org'
        )
        
        sailsResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a disability',
            type: 'disability'
        ))
        
        sailsResource.save(failOnError: true)
        
        // Sunshine Cottage School for Deaf Children - Education/Training
        def sunshineCottageResource = new Resource(
            name: 'Sunshine Cottage School for Deaf Children',
            description: 'Sunshine Cottage promotes early identification and intervention for children who are deaf or hard of hearing, helping them develop their maximum potential through listening, spoken language, and literacy.',
            servicesOffered: '''• Early childhood education for deaf/hard of hearing children
• Auditory-verbal therapy
• Speech and language therapy
• Parent education and support programs
• Hearing aid and cochlear implant support
• Preschool through elementary education programs
• Summer programs and camps
• Community outreach and hearing screenings
• Professional development for educators''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 4:30 PM\nSummer programs available',
            category: categories['Education/Training']
        )
        
        sunshineCottageResource.contact = new Contact(
            phone: '(210) 824-0579',
            email: 'info@sunshinecottage.org',
            address: '603 E Hildebrand Ave',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78212',
            website: 'https://www.sunshinecottage.org'
        )
        
        sunshineCottageResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be deaf or hard of hearing',
            type: 'disability'
        ))
        sunshineCottageResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Ages 0-11 years for educational programs',
            type: 'age'
        ))
        
        sunshineCottageResource.save(failOnError: true)
        
        // STRAPS - South Texas Regional Adaptive & Para Sports - Support/Community
        def strapsResource = new Resource(
            name: 'STRAPS - South Texas Regional Adaptive & Para Sports',
            description: 'STRAPS organizes, promotes, and conducts a variety of adaptive and Paralympic-style sports for wounded service members, veterans, and other adults and youth with physical disabilities.',
            servicesOffered: '''• Wheelchair basketball leagues and tournaments
• Wheelchair rugby programs
• Wheelchair soccer and football
• Adaptive softball and baseball
• Sit volleyball programs
• Power soccer for power wheelchair users
• Goalball for visually impaired athletes
• Boccia programs
• Track and field training
• Adaptive pickleball and tennis
• Functional fitness programs
• Equipment loans for sports participation''',
            hoursOfOperation: 'Program times vary - Check website for schedule',
            category: categories['Support/Community']
        )
        
        strapsResource.contact = new Contact(
            phone: '(210) 495-5888',
            email: 'info@strapsports.org',
            address: '5025 David Edwards Dr',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78233',
            website: 'https://www.strapsports.org'
        )
        
        strapsResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a physical disability or visual impairment',
            type: 'disability'
        ))
        
        strapsResource.save(failOnError: true)
        
        // Disability Services of the Southwest - Multiple Categories
        def dsswResource = new Resource(
            name: 'Disability Services of the Southwest (DSSW)',
            description: 'Founded in 1993, DSSW is one of the largest providers of support services to people with disabilities and the elderly in Texas, offering comprehensive in-home and community-based services.',
            servicesOffered: '''• Personal attendant services
• In-home nursing care
• Physical, occupational, and speech therapies
• Home modification and accessibility improvements
• Adaptive aids and equipment
• Job coaching and supported employment
• Deaf-Blind with Multiple Disabilities (DBMD) services
• Respite care services
• Case management
• Transportation assistance
• Community integration support''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 5:00 PM\n24/7 on-call for emergencies',
            featured: true,
            category: categories['Support/Community']
        )
        
        dsswResource.contact = new Contact(
            phone: '(210) 674-3977',
            email: 'info@dssw.org',
            address: '1839 NE Loop 410, Suite 107',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78217',
            website: 'https://www.dssw.org'
        )
        
        dsswResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have a disability or be elderly requiring support services',
            type: 'disability'
        ))
        
        dsswResource.save(failOnError: true)
        
        // AccessAbility - Support/Community
        def accessAbilityResource = new Resource(
            name: 'AccessAbility Home Modifications',
            description: 'AccessAbility provides home safety modifications for seniors and people with disabilities in San Antonio, helping them remain safely in their homes through accessibility improvements.',
            servicesOffered: '''• Wheelchair ramp installation
• Bathroom safety modifications
• Grab bar and handrail installation
• Door widening for wheelchair access
• Threshold removal and modifications
• Lever handle conversions
• Accessible shower installations
• Stair lift evaluations
• Home accessibility assessments
• Emergency repair services''',
            hoursOfOperation: 'Monday-Friday 8:00 AM - 4:30 PM',
            category: categories['Support/Community']
        )
        
        accessAbilityResource.contact = new Contact(
            phone: '(210) 477-3275',
            email: 'info@accessabilitysa.org',
            address: '8130 Broadway St, Suite 300',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78209',
            website: 'https://www.accessabilitysa.org'
        )
        
        accessAbilityResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must have mobility limitations or disability requiring home modifications',
            type: 'disability'
        ))
        accessAbilityResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Income eligibility may apply for free services',
            type: 'income'
        ))
        
        accessAbilityResource.save(failOnError: true)
        
        // Respite Care of San Antonio - Support/Community
        def respiteCareResource = new Resource(
            name: 'Respite Care of San Antonio',
            description: 'Respite Care of San Antonio provides temporary relief for caregivers of children with special needs and adults with disabilities, offering both in-home and facility-based respite services.',
            servicesOffered: '''• In-home respite care services
• Saturday respite program at facility
• Summer camp programs
• After-school programs
• Parent Night Out events
• Caregiver support groups
• Emergency respite services
• Trained respite care providers
• Social activities for clients
• Family support and resources''',
            hoursOfOperation: 'Office: Monday-Friday 8:00 AM - 5:00 PM\nRespite services available evenings and weekends',
            category: categories['Support/Community']
        )
        
        respiteCareResource.contact = new Contact(
            phone: '(210) 737-1212',
            email: 'info@respitecaresa.org',
            address: '5038 Wurzbach Rd',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78238',
            website: 'https://www.respitecaresa.org'
        )
        
        respiteCareResource.addToEligibilityRequirements(new EligibilityRequirement(
            requirement: 'Must be caring for someone with special needs or disabilities',
            type: 'other'
        ))
        
        respiteCareResource.save(failOnError: true)
        
        log.info "Added 7 additional disability services to the database (January 2025)"
    }
}