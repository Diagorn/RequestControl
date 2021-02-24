package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.Human;
import ru.mpei.requests.domain.users.Role;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.HumanRepo;
import ru.mpei.requests.repos.UserRepo;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service //All the business logic for users
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo; //Used to work with users

    @Autowired
    private ChatRepo chatRepo; //Used to work with chats

    @Autowired
    private HumanRepo humanRepo;

    @Autowired
    private HumanService humanService; //Used to work with persons

    @Value("${upload.path}")
    private String uploadPath; //Path for uploading files

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder; //Used to encode passwords

    public User findUserByID(Long Id) {
        Optional<User> user = userRepo.findById(Id);
        return user.orElse(null);
    }

    public void setUserAvatar(User user, MultipartFile file) throws IOException { //Setting the profile image for the user
        File uploadDir = new File(uploadPath + File.separator + "img" + File.separator);

        if(!uploadDir.exists()) { //If the file directory does not exist
            uploadDir.mkdir(); //We make it
        }

        String resultFileName = "";

        if(file != null && !file.getOriginalFilename().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
            resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + File.separator + "img"+ File.separator + resultFileName));
        } else {
            resultFileName = "defaultavatar.png";
        }
        user.setFileName(resultFileName);
    }

    public boolean isPossibleToCreateAUser(String username) { //If the user already exists in DB we cannot use this login
        User userFromDB = userRepo.findByUsername(username);
        return userFromDB == null;
    }

    public User createUser(String username, String password, boolean isOrganisation) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);
        user.setPhysical(!isOrganisation);
        user.setRoles(new HashSet<Role>());
        user.getRoles().add(Role.USER);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOrganisation(null);
        user.setPerson(null);

        userRepo.save(user);

        return user;

        //Gotta send the letter to the user with the activation code
    }

    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void createUserFromAdminPanel(User user,
                                         String email,
                                         String firstName,
                                         String secondName,
                                         String lastName) throws IOException {
        user.setUsername(email);
        user.getPerson().setLastName(lastName);
        user.getPerson().setSecondName(secondName);
        user.getPerson().setFirstName(firstName);

        humanService.updateHuman(user.getPerson());

        setUserAvatar(user, null);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode("123")); //For the first login
        Chat chat = chatRepo.findByOrganisationRequestIsNullAndPhysicalRequestIsNull();
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.MODER)) {
            chat.getMembers().add(user);
        }
        userRepo.save(user);
        chatRepo.save(chat);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException { //Used for security purposes
        return userRepo.findByUsername(s);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) { //Saving the user from admin panel
        user.setUsername(username);
        setUserRolesFromRegForm(user, form);
        Chat chat = chatRepo.findById(0L).get();
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.MODER)) {
            chat.getMembers().add(user);
            chatRepo.save(chat);
        }
        userRepo.save(user);
    }

    public void setUserRolesFromRegForm(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet()); //Getting the list of all roles from the form as a set
        user.setRoles(new HashSet<>());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
    }

    @Transactional
    public void deleteUsersByNames(List<String> name) { //IS BUGGED
        for (String s: name) {
            userRepo.deleteByUsername(s);
        }
    }



    public User findUserByQuery(String name) { //Getting one particular user by a string
        User user;
        user = userRepo.findByUsername(name);
        if (user == null) {
            user = userRepo.findByUsernameContaining(name);
        }
        if (user == null) {
            Human human = humanRepo.findByFirstNameContaining(name);
            user = userRepo.findByPerson(human);
        }
        if (user == null) {
            Human human = humanRepo.findBySecondNameContaining(name);
            user = userRepo.findByPerson(human);
        }
        if (user == null) {
            Human human = humanRepo.findByLastNameContaining(name);
            user = userRepo.findByPerson(human);
        }
        return user;
    }

    public String getStringRoleName(Role role) { //Giving the roles russian analogues
        if (role == Role.ADMIN)
            return "Администратор";
        if (role == Role.MODER)
            return "Модератор";
        if (role == Role.CLIENT)
            return "Клиент";
        if (role == Role.EXECUTER)
            return "Исполнитель";
        if (role == Role.USER)
            return "Пользователь";
        return "";
    }



    public List<User> getAllUsersByQuery(String query) { //Get all users by a string
        List<Human> firstNameList = humanRepo.findAllByFirstNameContaining(query);
        List<Human> secondNameList = humanRepo.findAllBySecondNameContaining(query);
        List<Human> lastNameList = humanRepo.findAllByLastNameContaining(query);

        List<User> firstNameUserList = new ArrayList<User>();
        List<User> secondNameUserList = new ArrayList<User>();
        List<User> lastNameUserList = new ArrayList<User>();

        for (Human human : firstNameList) {
            User user = userRepo.findByPerson(human);
            firstNameUserList.add(user);
        }

        for (Human human : secondNameList) {
            User user = userRepo.findByPerson(human);
            secondNameUserList.add(user);
        }

        for (Human human : lastNameList) {
            User user = userRepo.findByPerson(human);
            lastNameUserList.add(user);
        }

        List<User> usernameList = userRepo.findAllByUsernameContaining(query);
        List<User> resultList = ServiceUtils.collideLists(firstNameUserList, secondNameUserList);
        ServiceUtils.collideLists(resultList, lastNameUserList);
        ServiceUtils.collideLists(resultList, usernameList);
        return resultList;
    }

    public List<User> getAllModersAndAdmins() {
        List<User> admins = userRepo.findAllByRoles(Role.ADMIN);
        List<User> moders = userRepo.findAllByRoles(Role.MODER);
        return ServiceUtils.collideLists(admins, moders);
    }

    public List<User> getAllExecuters() {
        return userRepo.findAllByRoles(Role.EXECUTER);
    }

    public List<User> getExecutersByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        users.removeIf(u -> !u.isExecuter());
        return users;
    }

    public List<User> getModersAndAdminsByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        users.removeIf(u -> !u.isAdmin() && !u.isModer());
        return users;
    }

    public User getClientByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        for (User user : users) {
            if (user.isClient())
                return user;
        }
        return null;
    }

    public void updateProfile(User user, String firstName, String secondName,
                              String lastName, String telephone, String dob,
                              String sex, String passport, String passportDate,
                              String passportOrgan, String citizenship,
                              String adress, String index, String education,
                              String speciality, String groupName)  {
        Human p = user.getPerson();
        p.setFirstName(firstName);
        p.setSecondName(secondName);
        p.setLastName(lastName);
        p.setPhoneNumber(telephone);
        try {
            if (!dob.equals("1"))
                p.setDOB(ServiceUtils.parseStringToCalendar(dob));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setSex(sex.equals("male"));
        p.setPassport(passport);
        try {
            if (!passportDate.equals("1"))
                p.setPassportDate(ServiceUtils.parseStringToCalendar(passportDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setPassportGivingOrgan(passportOrgan);
        p.setCitizenship(citizenship);
        p.setRegistrationAdress(adress);
        p.setIndex(index);
        if (!education.equals("1"))
            p.setEducation(education);
        p.setSpeciality(speciality);
        p.setGroup(groupName);
        humanRepo.save(p);
        userRepo.save(user);
    }
}