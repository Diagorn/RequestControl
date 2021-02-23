package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.HumanRepo;
import ru.mpei.requests.repos.UserRepo;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

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



    public void saveDirectorOfOrganisation(Organisation organisation, String firstName, String secondName, String lastName, String position, String phone) {
        Human human = new Human();
        User user = organisation.getUser();

        human.setFirstName(firstName);
        human.setSecondName(secondName);
        human.setLastName(lastName);
        human.setPosition(position);
        human.setPhoneNumber(phone);
        human.setUser(user);
        human.setOrganisation(organisation);

        humanRepo.save(human);
        userRepo.save(user);

        organisation.setDirector(human);
        organisationService.updateOrganisation(organisation);
    }



    public void updateHuman(Human person) {
        humanRepo.save(person);
    }

    public Human createEmployee(String lastName, String firstName, String secondName, String telephone, String email, String passport, String adress, String education, String dob) throws ParseException {
        Calendar DOB = ServiceUtils.parseStringToCalendar(dob);

        Human human = new Human();

        human.setLastName(lastName);
        human.setFirstName(firstName);
        human.setSecondName(secondName);
        human.setPhoneNumber(telephone);
        human.setEmail(email);
        human.setPassport(passport);
        human.setRegistrationAdress(adress);
        human.setEducation(education);
        human.setDOB(DOB);
        humanRepo.save(human);

        return human;
    }

    public void saveHumanFromForm(String username, String password, String lastName, String firstName, String secondName, String telephone, String passport,
                                  String adress, String education, String dob, MultipartFile avatar, String passportDate, String passportOrgan, String index,
                                  String citizenship, String speciality, String groupName, String sex) throws ParseException, IOException {
        User user;
        if (!userService.isPossibleToCreateAUser(username))
            user = userService.findUserByUsername(username);
        else
            user = userService.createUser(username, password, false);

        Calendar DOB = ServiceUtils.parseStringToCalendar(dob);
        Calendar passDate = ServiceUtils.parseStringToCalendar(passportDate);

        Human human = new Human();
        human.setLastName(lastName);
        human.setFirstName(firstName);
        human.setSecondName(secondName);
        human.setPhoneNumber(telephone);
        human.setPassport(passport);
        human.setRegistrationAdress(adress);
        human.setEducation(education);
        human.setDOB(DOB);
        human.setUser(user);
        human.setEmail(user.getUsername());
        human.setPassportDate(passDate);
        human.setPassportGivingOrgan(passportOrgan);
        human.setIndex(index);
        human.setCitizenship(citizenship);
        human.setSpeciality(speciality);
        human.setGroup(groupName);
        human.setSex(sex.equals("male"));

        humanRepo.save(human);

        user.setPerson(human);
        userService.setUserAvatar(user, avatar);
        userRepo.save(user);
    }
}