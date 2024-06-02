//package com.azat4dev.booking.listingsms.helpers;
//
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.localstack.LocalStackContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//import org.testcontainers.utility.MountableFile;
//
//import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
//
//@Testcontainers
//public interface LocalStackTests {
//
//    DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");
//
//    @Container
//    LocalStackContainer localStack = new LocalStackContainer(localstackImage)
//        .withServices(S3)
//        .withCopyFileToContainer(
//            MountableFile.forClasspathResource("localstack/init.sh"),
//            "/etc/localstack/init/ready.d/init.sh"
//        );
//
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("app.objects_storage.bucket.listings-photo.endpoint", localStack:: getEndpoint);
//        registry.add("app.objects_storage.bucket.listings-photo.access-key", localStack:: getAccessKey);
//        registry.add("app.objects_storage.bucket.listings-photo.secret-key", localStack:: getSecretKey);
//    }
//}