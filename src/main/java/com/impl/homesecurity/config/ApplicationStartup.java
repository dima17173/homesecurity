package com.impl.homesecurity.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.impl.homesecurity.domain.AppData;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.service.AppDataLoraDBService;
import com.impl.homesecurity.service.ApplicationDataService;
import com.impl.homesecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Created by dima.
 * Creation date 19.10.18.
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${name}")
    private String name;
    @Value("${login}")
    private String login;
    @Value("${pass}")
    private String password;

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class.getName());
    private final UserService userService;
    private PasswordEncoder passwordEncoder;
    private static FirebaseOptions options;
    private final ApplicationContext applicationContext;
    private final ApplicationDataService applicationDataService;
    private final AppDataLoraDBService appDataLoraDBService;

    @Autowired
    public ApplicationStartup(UserService userService, PasswordEncoder passwordEncoder, ApplicationContext applicationContext,
                              ApplicationDataService applicationDataService, AppDataLoraDBService appDataLoraDBService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.applicationContext = applicationContext;
        this.applicationDataService = applicationDataService;
        this.appDataLoraDBService = appDataLoraDBService;
    }

    /**
     * Инициализация приложения
     * @param applicationReadyEvent Событие готовности приложения
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Application startup event triggered------------- ");
            addUserWithRoleSuperAdmin();
            loadDataFromLoraDB();
        try {
        if (Objects.isNull(options)) {
            options = getFirebaseOption();
            FirebaseApp.initializeApp(options);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Синхронизация данных с базой сигналов LoraDB
     */
    private void loadDataFromLoraDB() {
        AppData lastDeviceSignal = applicationDataService.getLastDeviceSignal();
        if (lastDeviceSignal == null){
            AppData lastDeviceSignalLoraDB = appDataLoraDBService.getLastDeviceSignalLoraDB();
            if(lastDeviceSignalLoraDB == null){
                if (log.isErrorEnabled()) {
                    log.error("Table appdata from LoraDB is empty");
                }
            } else {
                applicationDataService.addAppData(lastDeviceSignalLoraDB);
            }
        }
    }

    /**
     * Создает СуперАдмина один раз при старте приложения, если такого нет в базе
     */
    private void addUserWithRoleSuperAdmin() {

        List<User> users = userService.getAllUsers();
        for (User currentUser : users) {
            if (currentUser.getRole().getRoleName().equals("ROLE_SUPERADMIN")) {
                return;
            }
        }

        User superAdmin = new User();

        superAdmin.setId(null);
        superAdmin.setLogin(login);
        superAdmin.setName(name);
        superAdmin.setPassword(passwordEncoder.encode(password));
        superAdmin.setFirstSignIn(false);
        superAdmin.setRole(Role.ROLE_SUPERADMIN);
        superAdmin.setMainUser(new User());

        userService.addUser(superAdmin);
    }

    /**
     * Настройки Firebase
     * home-security-477d9-firebase-adminsdk-exef0-17fbc07c6f.json - файл настроек с консоли Firebase
     * @return Обьект настроек Firebase
     * @throws IOException
     */
    private FirebaseOptions getFirebaseOption() throws IOException {
        Resource resource = applicationContext.getResource("classpath:home-security-477d9-firebase-adminsdk-exef0-17fbc07c6f.json");
        InputStream resourceInputStream = resource.getInputStream();
      return new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(resourceInputStream))
                .build();
    }
}
