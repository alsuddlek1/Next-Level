import { useSituationResultGetHook } from "@/hooks/mypage/useSituationResultGetHook";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  StyledClipBox,
  StyledClipContainer,
  StyledImageBox,
  StyledImage,
  StyledContentContainer,
  StyledTtile,
  StyledClipButton,
} from "../myshadowing/MyShadowing.styled";
import { S3_ADDRESS } from "@/api/api";

const MySituation = () => {
  const { result, getSituationResult } = useSituationResultGetHook();
  const navigate = useNavigate();
  useEffect(() => {
    getSituationResult();
  }, []);
  return (
    <StyledClipContainer>
      {result?.map((clip: any) => {
        return (
          <StyledClipBox
            key={clip.id}
            // onClick={() => navigate(`/drama/shadowing/${clip.id}`)}
          >
            <StyledImageBox>
              <StyledImage src={S3_ADDRESS + clip.image}></StyledImage>
            </StyledImageBox>
            <StyledContentContainer>
              <StyledTtile>{clip.title}</StyledTtile>
              <StyledClipButton>학습하기</StyledClipButton>
            </StyledContentContainer>
          </StyledClipBox>
        );
      })}
    </StyledClipContainer>
  );
};

export default MySituation;