package server.api.pingpong.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.response.FakerApiResponse;
import server.api.pingpong.domain.dto.response.GetAllUsersResponse;
import server.api.pingpong.domain.dto.request.InitRequest;
import server.api.pingpong.domain.dto.UserDto;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.repository.UserRepository;
import server.api.pingpong.domain.state.Status;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    public ResponseType saveMemberInfoFromApiCall(InitRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakerapi.it/api/v1/users?_seed=" + request.getSeed() + "&_quantity=" + request.getQuantity() + "&_locale=ko_KR";
        FakerApiResponse response = restTemplate.getForObject(url, FakerApiResponse.class);

        if (response != null && response.getData() != null) {
            List<FakerApiResponse.Faker> sortedData = response.getData().stream()
                    .sorted(Comparator.comparing(FakerApiResponse.Faker::getId))
                    .collect(Collectors.toList());

            for (FakerApiResponse.Faker newData : sortedData) {
                userRepository.save(
                        User.builder()
                                .fakerId(newData.getId())
                                .name(newData.getUsername())
                                .email(newData.getEmail())
                                .status(getStatusByFakerId(newData.getId()))
                                .build()); //TODO 전체 저장(saveAll)과 개별 저장 시 어느쪽이 성능상 우세할까?
            }
        } else {
            return ResponseType.SERVER_ERROR;
        }
        return ResponseType.REQUEST_SUCCESS;
    }

    private Status getStatusByFakerId(int fakerId) {
        if (fakerId <= 30) {
            return Status.ACTIVE;
        } else if (fakerId <= 60) {
            return Status.WAIT;
        } else {
            return Status.NON_ACTIVE;
        }
    }
    public GetAllUsersResponse getUserPage(int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAll(pageable);
        return GetAllUsersResponse.builder()
                .totalElements((int)userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .userList(userPage.getContent().stream().map(UserDto::of).collect(Collectors.toList())
                )
                .build();
    }

    public User isActive(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResponseType.BAD_REQUEST));
        if(!user.getStatus().equals(Status.ACTIVE)){
            throw new BizException(ResponseType.BAD_REQUEST);
        }
        return user;
    }

    public User findUser(int userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResponseType.BAD_REQUEST));
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
