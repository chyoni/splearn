package cwchoiit.splearn.member.adapter.in.web;

import cwchoiit.splearn.member.adapter.in.web.response.MemberRegisterResponse;
import cwchoiit.splearn.member.application.provided.MemberRegisterUseCase;
import cwchoiit.splearn.member.domain.Member;
import cwchoiit.splearn.member.domain.payload.MemberRegisterPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberRegisterUseCase memberRegisterUseCase;

    @PostMapping
    public ResponseEntity<MemberRegisterResponse> register(
            @RequestBody @Valid MemberRegisterPayload payload) {
        Member member = memberRegisterUseCase.register(payload);
        return ResponseEntity.ok(MemberRegisterResponse.of(member));
    }
}
