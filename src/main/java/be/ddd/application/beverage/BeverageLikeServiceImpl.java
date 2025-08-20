package be.ddd.application.beverage;

import be.ddd.api.dto.res.BeverageLikeDto;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.MemberBeverageLike;
import be.ddd.domain.exception.AlreadyLikeBeverageException;
import be.ddd.domain.exception.CafeBeverageNotFoundException;
import be.ddd.domain.exception.LikeNotFoundException;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.MemberBeverageLikeRepository;
import be.ddd.domain.repo.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BeverageLikeServiceImpl implements BeverageLikeService {

    private final MemberBeverageLikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final CafeBeverageRepository beverageRepository;

    @Override
    public BeverageLikeDto likeBeverage(Long memberId, UUID productId) {
        Member member = findMember(memberId);
        CafeBeverage bev = findBeverage(productId);

        if (likeRepository.existsByMemberIdAndBeverageProductId(memberId, productId)) {
            throw new AlreadyLikeBeverageException();
        }

        likeRepository.save(new MemberBeverageLike(member, bev));
        return toDto(bev.getProductId(), true);
    }

    @Override
    public BeverageLikeDto unlikeBeverage(Long memberId, UUID productId) {
        if (!likeRepository.existsByMemberIdAndBeverageProductId(memberId, productId)) {
            throw new LikeNotFoundException();
        }
        likeRepository.deleteByMemberIdAndBeverageProductId(memberId, productId);
        return toDto(productId, false);
    }

    private Member findMember(Long memberId) {
        return memberRepository
                .findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private CafeBeverage findBeverage(UUID productId) {
        return beverageRepository
                .findByProductId(productId)
                .orElseThrow(CafeBeverageNotFoundException::new);
    }

    private BeverageLikeDto toDto(UUID productId, boolean liked) {
        long likeCount = likeRepository.countByBeverageProductId(productId);
        return new BeverageLikeDto(productId, liked, likeCount);
    }
}
