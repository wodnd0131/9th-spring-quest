package server.api.pingpong.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.client.RestTemplate;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.request.InitRequest;
import server.api.pingpong.domain.dto.response.FakerApiResponse;
import server.api.pingpong.domain.dto.response.GetAllUsersResponse;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.repository.UserRepository;
import server.api.pingpong.domain.state.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveMemberInfoFromApiCall() {
        // Given
        InitRequest request = InitRequest.builder()
                .seed(1).quantity(5).build();

        FakerApiResponse response = new FakerApiResponse();
        List<FakerApiResponse.Faker> data = new ArrayList<>();
        for (int i = 1; i <= request.getQuantity(); i++) {
            FakerApiResponse.Faker faker = new FakerApiResponse.Faker();
            faker.setId(i);
            faker.setUsername("User " + i);
            faker.setEmail("user" + i + "@example.com");
            data.add(faker);
        }
        response.setData(data);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakerapi.it/api/v1/users?_seed=" + request.getSeed() + "&_quantity=" + request.getQuantity() + "&_locale=ko_KR";
        restTemplate.getForObject(url, FakerApiResponse.class);


        // When
        ResponseType result = userService.saveMemberInfoFromApiCall(request);

        // Then
        assertEquals(ResponseType.REQUEST_SUCCESS, result);
        verify(userRepository, times(request.getQuantity())).save(any(User.class));
    }

    @Test
    void getUserPage() {
        // Given
        int size = 10;
        int page = 1;
        int totalElements = 20; // 전체 요소 수를 20으로 설정
        int totalPages = (int) Math.ceil((double) totalElements / size); // 전체 페이지 수를 계산
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            User user = new User().builder()
                    .id(i).status(Status.ACTIVE).build();
            users.add(user);
        }
        Page<User> userPage = new PageImpl<>(users, pageable, totalElements); // totalElements 값을 사용

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // When
        GetAllUsersResponse result = userService.getUserPage(size, page);

        // Then
        assertEquals(totalElements, result.getTotalElements());
        assertEquals(totalPages, result.getTotalPages());
        assertEquals(users.size(), result.getUserList().size());
    }


    @Test
    void isActive() {
        // Given
        int userId = 1;
        User user = new User().builder()
                .id(userId).status(Status.ACTIVE).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userService.isActive(userId);

        // Then
        assertEquals(user, result);
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    void findUser() {
        // Given
        int userId = 1;
        User user = new User().builder()
                .id(userId).status(Status.ACTIVE).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userService.findUser(userId);

        // Then
        assertEquals(user, result);
    }

    @Test
    void deleteAll() {
        // Given
        doNothing().when(userRepository).deleteAll();

        // When
        userService.deleteAll();

        // Then
        verify(userRepository, times(1)).deleteAll();
    }
}