package domain;

import domain.JobOfferService;

public class JobOfferServiceTest {
    private JobOfferService service;

    @BeforeAll
    void beforeAll() {
        service = new JobOfferService();
    }

    @Test
    void addJobOfferTest() {
        boolean result = service.addJobOffer("Software Engineer", "Remote position");
        Assertions.assertEquals(true, result);
    }

    @AfterAll
    void afterAll() {
        System.out.println("Tests finished");
    }
}
