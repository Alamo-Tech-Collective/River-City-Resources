package riverCityResources

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class BootstrapServiceSpec extends Specification implements ServiceUnitTest<BootstrapService>{

    def setup() {
    }

    def cleanup() {
    }

    void "test service method exists"() {
        expect:"service method should exist and be callable"
            service.respondsTo('serviceMethod')
    }
    
    void "test service is properly initialized"() {
        expect:"service should be initialized"
            service != null
            service instanceof BootstrapService
    }
}
