package com.example.sbchainssioicdoauth2.model.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SsiApplication {

    @Id
    private String id;

    private String uuid;

    //personal info
    private String ssn;
    private String taxisAfm;
    private String taxisFamilyName;
    private String taxisFirstName;
    private String taxisFathersName;
    private String taxisMothersName;
    private String taxisFamilyNameLatin;
    private String taxisFirstNameLatin;
    private String taxisFathersNameLatin;
    private String taxisMothersNameLatin;
    private String taxisAmka;
    private String taxisDateOfBirth;
    private String taxisGender;
    private String nationality;
    private String maritalStatus;

    //personal declaration
    private String hospitalized;
    private String hospitalizedSpecific;
    private String monk;
    private String luxury;

    //residence info
    private String street;
    private String streetNumber;
    private String po;
    private String municipality;
    private String prefecture;
    private String ownership;
    private String supplyType;
    private String meterNumber;

    //Fead
    private String participateFead;
    private String selectProvider;

    //demographic info
    private String gender;
    private String disabilityStatus;
    private String levelOfEducation;

    //employment info
    private String employmentStatus;
    private String unemployed;
    private String employed;
    private String oaedId;
    private String oaedDate;

    //contact info
    private String email;
    private String mobilePhone;
    private String landline;
    private String iban;
    private Map<String, String> mailAddress = new HashMap<>();

    //parenthood info
    private String parenthood;
    private String custody;
    private String additionalAdults;

    //financial info
    private String salariesR;
    private String pensionsR;
    private String farmingR;
    private String freelanceR;
    private String rentIncomeR;
    private String unemploymentBenefitR;
    private String otherBenefitsR;
    private String ekasR;
    private String otherIncomeR;
    private String ergomeR;
    private String depositInterestA;
    private String depositsA;
    private String domesticRealEstateA;
    private String foreignRealEstateA;
    private String vehicleValueA;
    private String investmentsA;

    //household composition
    private Map<String, String>[] householdComposition;

    //income guarantee
    private String monthlyGuarantee;
    private String totalIncome;
    private String monthlyIncome;
    private String monthlyAid;

    // helper object
    private boolean savedInDb;

    //the status of the application
    private String status;

}
