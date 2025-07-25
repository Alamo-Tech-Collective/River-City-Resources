package com.rivercity

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ResourceSearchServiceSpec extends Specification implements ServiceUnitTest<ResourceSearchService> {

    def setup() {
    }

    def cleanup() {
    }

    void "test search with query returns matching resources"() {
        given:
        def category = new ResourceCategory(name: "Healthcare", description: "Healthcare services")
        category.save(flush: true)
        
        def resource1 = new Resource(
            name: "Test Clinic",
            description: "Medical services",
            address: "123 Main St",
            city: "San Antonio",
            state: "TX",
            zipCode: "78201",
            phone: "210-555-0001",
            website: "http://testclinic.com",
            services: "Primary care, diagnostics",
            category: category
        )
        resource1.save(flush: true)
        
        def resource2 = new Resource(
            name: "Another Service",
            description: "Different service",
            address: "456 Oak St",
            city: "San Antonio",
            state: "TX",
            zipCode: "78202",
            phone: "210-555-0002",
            website: "http://another.com",
            services: "Support services",
            category: category
        )
        resource2.save(flush: true)

        when:
        def results = service.search("clinic")

        then:
        results.total == 1
        results.results.size() == 1
        results.results[0].name == "Test Clinic"
    }

    void "test search with category filter"() {
        given:
        def category1 = new ResourceCategory(name: "Healthcare", description: "Healthcare services")
        category1.save(flush: true)
        
        def category2 = new ResourceCategory(name: "Education", description: "Education services")
        category2.save(flush: true)
        
        def resource1 = new Resource(
            name: "Health Center",
            description: "Medical services",
            address: "123 Main St",
            city: "San Antonio",
            state: "TX",
            zipCode: "78201",
            phone: "210-555-0001",
            website: "http://health.com",
            services: "Healthcare",
            category: category1
        )
        resource1.save(flush: true)
        
        def resource2 = new Resource(
            name: "Learning Center",
            description: "Education services",
            address: "456 Oak St",
            city: "San Antonio",
            state: "TX",
            zipCode: "78202",
            phone: "210-555-0002",
            website: "http://learn.com",
            services: "Education",
            category: category2
        )
        resource2.save(flush: true)

        when:
        def results = service.search(null, category1.id)

        then:
        results.total == 1
        results.results.size() == 1
        results.results[0].category.id == category1.id
    }

    void "test pagination"() {
        given:
        def category = new ResourceCategory(name: "Services", description: "General services")
        category.save(flush: true)
        
        20.times { i ->
            new Resource(
                name: "Resource ${i}",
                description: "Description ${i}",
                address: "${i} Main St",
                city: "San Antonio",
                state: "TX",
                zipCode: "78201",
                phone: "210-555-${String.format('%04d', i)}",
                website: "http://resource${i}.com",
                services: "Service ${i}",
                category: category
            ).save(flush: true)
        }

        when:
        def results = service.search(null, null, [max: 5, offset: 10])

        then:
        results.total == 20
        results.results.size() == 5
    }
}