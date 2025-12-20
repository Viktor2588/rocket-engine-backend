package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.LaunchSite;
import com.rocket.comparison.entity.LaunchSiteStatus;
import com.rocket.comparison.repository.LaunchSiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Seeds LaunchSite entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LaunchSiteSeeder implements EntitySeeder {

    private final LaunchSiteRepository launchSiteRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding launch sites...");
            seedLaunchSites();
            log.info("Seeded {} launch sites", count());
        }
    }

    @Override
    public String getEntityName() {
        return "launch sites";
    }

    @Override
    public long count() {
        return launchSiteRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedLaunchSites() {
        createLaunchSite("Kennedy Space Center", getCountryMap().get("USA"), "Florida", "NASA",
            28.5729, -80.6490, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1962, 1000, "Historic launch site for Apollo and Space Shuttle.");

        createLaunchSite("Cape Canaveral SFS", getCountryMap().get("USA"), "Florida", "US Space Force",
            28.4889, -80.5778, LaunchSiteStatus.OPERATIONAL, true, true, false, false,
            1950, 900, "America's busiest spaceport.");

        createLaunchSite("Vandenberg SFB", getCountryMap().get("USA"), "California", "US Space Force",
            34.7420, -120.5724, LaunchSiteStatus.OPERATIONAL, true, false, true, false,
            1958, 700, "Primary west coast launch site.");

        createLaunchSite("Baikonur Cosmodrome", getCountryMap().get("RUS"), "Kazakhstan", "Roscosmos",
            45.9650, 63.3050, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1955, 1500, "World's oldest and largest launch facility.");

        createLaunchSite("Plesetsk Cosmodrome", getCountryMap().get("RUS"), "Arkhangelsk Oblast", "Russian Space Forces",
            62.9258, 40.5778, LaunchSiteStatus.OPERATIONAL, false, false, true, false,
            1957, 1600, "Northern Russian launch site.");

        createLaunchSite("Jiuquan", getCountryMap().get("CHN"), "Inner Mongolia", "CNSA",
            40.9606, 100.2914, LaunchSiteStatus.OPERATIONAL, true, false, false, true,
            1958, 200, "China's oldest launch center.");

        createLaunchSite("Wenchang", getCountryMap().get("CHN"), "Hainan", "CNSA",
            19.6145, 110.9510, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            2014, 15, "China's newest and largest launch site.");

        createLaunchSite("Guiana Space Centre", getCountryMap().get("ESA"), "French Guiana", "CNES/Arianespace",
            5.2322, -52.7693, LaunchSiteStatus.OPERATIONAL, true, true, true, false,
            1968, 300, "European spaceport near the equator.");

        createLaunchSite("Tanegashima", getCountryMap().get("JPN"), "Kagoshima", "JAXA",
            30.4000, 130.9689, LaunchSiteStatus.OPERATIONAL, true, true, true, false,
            1969, 90, "Japan's largest launch center.");

        createLaunchSite("Satish Dhawan", getCountryMap().get("IND"), "Andhra Pradesh", "ISRO",
            13.7199, 80.2304, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1971, 100, "India's primary launch site.");

        createLaunchSite("Rocket Lab LC-1", getCountryMap().get("NZL"), "Hawkes Bay", "Rocket Lab",
            -39.2615, 177.8649, LaunchSiteStatus.OPERATIONAL, false, false, false, false,
            2016, 45, "World's first private orbital launch site.");
    }

    private void createLaunchSite(String name, Country country, String region, String operator,
            Double latitude, Double longitude, LaunchSiteStatus status,
            Boolean supportsInterplanetary, Boolean supportsGeo, Boolean supportsPolar,
            Boolean humanRated, Integer established, Integer totalLaunches, String description) {

        LaunchSite site = new LaunchSite();
        site.setName(name);
        site.setCountry(country);
        site.setRegion(region);
        site.setOperator(operator);
        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setStatus(status);
        site.setSupportsInterplanetary(supportsInterplanetary);
        site.setSupportsGeo(supportsGeo);
        site.setSupportsPolar(supportsPolar);
        site.setHumanRatedCapable(humanRated);
        site.setEstablishedYear(established);
        site.setTotalLaunches(totalLaunches);
        site.setDescription(description);
        launchSiteRepository.save(site);
    }
}
