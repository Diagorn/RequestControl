package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.HumanRepo;
import ru.mpei.requests.repos.OrganisationRepo;

@Service
public class OrganisationService {
    @Autowired
    private OrganisationRepo organisationRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private HumanRepo humanRepo;

    public void saveOrganisation(Long id, String name, String position, String physicalAdress, String legalAdress, String phone) {
        User user = userService.findUserByID(id);
        if (user != null) {
            Organisation organisation = new Organisation();
            organisation.setPosition(position);
            organisation.setName(name);
            organisation.setPhysicalAdress(physicalAdress);
            organisation.setLegalAdress(legalAdress);
            organisation.setPhoneNumber(phone);
            organisationRepo.save(organisation);
            userService.updateUser(user);
        }
    }

    public void saveDirector(Long id, String lastName, String firstName, String secondName, String phone) {
        User user = userService.findUserByID(id);
        if (user != null) {
            Human human = new Human();
            human.setLastName(lastName);
            human.setFirstName(firstName);
            human.setSecondName(secondName);
            human.setPhoneNumber(phone);
            humanRepo.save(human);
            user.getOrganisation().setDirector(human);
            userService.updateUser(user);
        }
    }
}
