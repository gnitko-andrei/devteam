package by.teachmeskills.devteam.dto.project;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectFiltersDtoUnitTest {

    @Test
    void shouldSetNameFilterValueNull_whenSetNameFilter_givenNullInput() {
        // given
        var givenDto = ProjectFiltersDto.builder().build();
        // when
        givenDto.setNameFilter(null);
        // then
        assertThat(givenDto.getNameFilter()).isNull();
    }

    @Test
    void shouldSetNameFilterValueNull_whenStNameFilter_givenBlankInput() {
        // given
        var givenDto = ProjectFiltersDto.builder().build();
        // when
        givenDto.setNameFilter(" ");
        // then
        assertThat(givenDto.getNameFilter()).isNull();
    }

    @Test
    void shouldSetNormalizedNameFilterValue_whenSetNameFilter_givenNameFilterInput() {
        // given
        var givenDto = ProjectFiltersDto.builder().build();
        // when
        givenDto.setNameFilter("   project NAME ");
        // then
        assertThat(givenDto.getNameFilter()).isEqualTo("project name");
    }

}