package by.teachmeskills.devteam.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectUnitTest {

    @Test
    void shouldReturnCustomerFullNameString_whenGetCustomerName_givenProjectCustomerSet() {
        // given
        var givenProject = new Project();
        final var givenFirstName = "firstName";
        final var givenLastName = "lastName";
        givenProject.setCustomer(User.builder().firstName(givenFirstName).lastName(givenLastName).build());
        // when
        final var actual = givenProject.getCustomerName();
        // then
        assertThat(actual).isEqualTo("firstName lastName");
    }


    @Test
    void shouldReturnNonePlaceholderString_whenGetCustomerName_givenProjectCustomerNotSet() {
        // given
        var givenProject = new Project();
        // when
        final var actual = givenProject.getCustomerName();
        // then
        assertThat(actual).isEqualTo("none");
    }

    @Test
    void shouldReturnManagerFullNameString_whenGetManagerName_givenProjectManagerSet() {
        // given
        var givenProject = new Project();
        final var givenFirstName = "firstName";
        final var givenLastName = "lastName";
        givenProject.setManager(User.builder().firstName(givenFirstName).lastName(givenLastName).build());
        // when
        final var actual = givenProject.getManagerName();
        // then
        assertThat(actual).isEqualTo("firstName lastName");
    }

    @Test
    void shouldReturnNonePlaceholderString_whenGetManagerName_givenProjectManagerNotSet() {
        // given
        var givenProject = new Project();
        // when
        final var actual = givenProject.getManagerName();
        // then
        assertThat(actual).isEqualTo("none");
    }

    @Test
    void shouldReturnSumOfAllTasksPrices_whenGetProjectPrice_givenProjectWithTasks() {
        // given
        var givenProject = new Project();
        var givenTasks = List.of(Task.builder().price(100).build(), Task.builder().price(111).build(), Task.builder().price(123).build());
        givenProject.setTasks(givenTasks);
        // when
        final var actual = givenProject.getProjectPrice();
        // then
        assertThat(actual).isEqualTo(334);
    }

    @Test
    void shouldReturnZero_whenGetProjectPrice_givenProjectWithoutTasks() {
        // given
        var givenProject = new Project();
        // when
        final var actual = givenProject.getProjectPrice();
        // then
        assertThat(actual).isZero();
    }
}