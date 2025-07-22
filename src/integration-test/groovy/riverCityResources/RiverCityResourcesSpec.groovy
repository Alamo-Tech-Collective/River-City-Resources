package riverCityResources
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import geb.spock.*

/**
 * See https://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class RiverCityResourcesSpec extends GebSpec {

    void "test homepage loads correctly"() {
        when:"The home page is visited"
            go '/'

        then:"The title is correct"
            title == "River City Resources - San Antonio Disability Resources Directory"
    }

}
