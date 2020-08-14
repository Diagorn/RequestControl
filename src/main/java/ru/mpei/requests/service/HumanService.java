package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.HumanRepo;
import ru.mpei.requests.repos.UserRepo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class HumanService {
    @Autowired
    private HumanRepo humanRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganisationService organisationService;

    public void saveHumanFromForm(String username, String password, String lastName, String firstName, String secondName, String telephone, int passport, String adress, String education, Date dob) throws IOException {
        if (!userService.isPossibleToCreateAUser(username))
            return;

        User user = userService.createUser(username, password, false);

        Calendar DOB = new GregorianCalendar(dob.getYear(), dob.getMonth(), dob.getDay());

        Human human = new Human();
        human.setLastName(lastName);
        human.setFirstName(firstName);
        human.setSecondName(secondName);
        human.setPhoneNumber(telephone);
        human.setPassport(passport);
        human.setRegistrationAdress(adress);
        human.setEducation(education);
        human.setDOB(DOB);

        humanRepo.save(human);

        user.setPerson(human);
        userRepo.save(user);
    }

    public void saveDirectorOfOrganisation(Organisation organisation, String firstName, String secondName, String lastName, String position, String phone) {
        Human human = new Human();
        User user = organisation.getUser();

        human.setFirstName(firstName);
        human.setSecondName(secondName);
        human.setLastName(lastName);
        human.setPosition(position);
        human.setPhoneNumber(phone);
        human.setUser(user);

        humanRepo.save(human);
        userRepo.save(user);

        organisation.setDirector(human);
        organisationService.updateOrganisation(organisation);
    }
}