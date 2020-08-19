package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.users.Organisation;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.OrganisationRepo;
import ru.mpei.requests.repos.UserRepo;

import java.io.IOException;

@Service
public class OrganisationService {
    @Autowired
    private OrganisationRepo organisationRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    public Organisation saveOrganisation(String username, String password, String name, String physicalAdress, String legalAdress, String phone, MultipartFile avatar) throws IOException {
        if (!userService.isPossibleToCreateAUser(username))
            return null;

        User user = userService.createUser(username, password, true);

        Organisation organisation = new Organisation();
        organisation.setName(name);
        organisation.setPhysicalAdress(physicalAdress);
        organisation.setLegalAdress(legalAdress);
        organisation.setPhoneNumber(phone);
        organisation.setUser(user);
        organisationRepo.save(organisation);

        user.setOrganisation(organisation);
        userService.setUserAvatar(user, avatar);
        userRepo.save(user);

        return organisation;
    }

    public void updateOrganisation(Organisation organisation) {
        organisationRepo.save(organisation);
    }
}