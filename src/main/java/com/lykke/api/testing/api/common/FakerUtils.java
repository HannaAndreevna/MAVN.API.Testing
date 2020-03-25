package com.lykke.api.testing.api.common;

import com.github.javafaker.Faker;
import com.lykke.api.testing.api.common.enums.CountryPhoneCodes;
import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FakerUtils {

    public int countryPhoneCode = CountryPhoneCodes.randomCountryCode();
    public int salesForceCountryPhoneCodeId = new Random().nextInt(100);
    //TODO: Refactor
    Faker faker = new Faker();
    public String firstName = faker.name().firstName();
    public String lastName = faker.name().lastName();
    public String randomQuote = faker.gameOfThrones().quote();
    public String localizationCode = faker.address().countryCode().toLowerCase();
    public String phoneNumber = faker.phoneNumber().phoneNumber().replace(".", "-");
    public String fullName = faker.name().fullName();
    public String country = faker.address().country();
    public String title = faker.book().title();
    public String address = faker.address().fullAddress();
    public String city = faker.address().cityName();
    public String companyName = faker.company().name();
}
