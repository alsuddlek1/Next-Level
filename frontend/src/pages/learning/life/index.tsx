import { useNavigate } from "react-router-dom";
import {
  StyledLearnLife,
  StyledLearnContainer,
  StyledLearnDirectMainBox,
  StyledLearnMainBox,
  StyledLearnIcon,
  StyledLearnContent,
  StyledLearnTitle,
  StyledLearnText,
  StyledLearnButton,
} from "./Life.styled";
import { useState } from "react";
import Modal from "@/components/modal";
import { useTranslation } from "react-i18next";
import { useChatbotHook } from "@/hooks/chatbot/useChatbotHook";
import { useChatbotTalkingHook } from "@/hooks/chatbot/useChatbotTalkingHook";
import LearningChatbot from "../chatbot";
import ChatbotModal from "../chatbot/chatbotmodal";

const LearningLife = () => {
  const { t } = useTranslation();
  const naviate = useNavigate();
  // 모달창
  const [isOpenModal, setIsOpenModal] = useState(false);

  const openModal = () => {
    setIsOpenModal(!isOpenModal);
  };
  const closeModal = () => {
    setIsOpenModal(!isOpenModal);
  };

  const openChat = () => {
    naviate("/learning/chatbot");
  };

  const openLifeChat = () => {
    naviate("/learning/lifechat");
  };

  // 챗봇 대화 주제 보내기
  const { firstQuestion, getChatbot } = useChatbotHook();
  // console.log(firstQuestion);
  // 잘나옴

  const handleSubmit = (e: any) => {
    e.preventDefault();
    getChatbot(e.target.name.value);
  };

  // 채팅창에 첫 대화 보내기
  const [isChatPage, setIsChatPage] = useState(false);
  const goChatpage = () => {
    setIsChatPage(!isChatPage);
  };

  return (
    <StyledLearnLife>
      {/* 대화생성 */}
      <StyledLearnContainer>
        <StyledLearnDirectMainBox>
          <StyledLearnMainBox id="chatbot">
            <StyledLearnIcon src="/learning/aibody.png" alt="food" />
            <StyledLearnContent>
              <StyledLearnTitle>{t("learning.direct.title")}</StyledLearnTitle>
              <StyledLearnText>{t("learning.direct.text")}</StyledLearnText>
            </StyledLearnContent>
          </StyledLearnMainBox>
          <StyledLearnButton onClick={openModal}>
            {t("learning.direct.start")}
          </StyledLearnButton>
        </StyledLearnDirectMainBox>
      </StyledLearnContainer>

      {/* 식당 */}
      <StyledLearnContainer id="restarant" onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/food.png" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.restaurant")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.restaurant")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>

      {/* 헬스장 */}
      <StyledLearnContainer onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/food.png" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.gym")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.gym")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>

      {/* 대중교통 */}
      <StyledLearnContainer onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/food.png" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.transport")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.transport")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>

      {/* 야구장 */}
      <StyledLearnContainer onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/baseball.svg" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.baseball")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.baseball")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>

      {/* 마트 */}
      <StyledLearnContainer onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/food.png" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.mart")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.mart")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>

      {/* 대학 */}
      <StyledLearnContainer onClick={openLifeChat}>
        <StyledLearnMainBox>
          <StyledLearnIcon src="/learning/food.png" alt="food" />
          <StyledLearnContent>
            <StyledLearnTitle>
              {t("learning.situation.title.university")}
            </StyledLearnTitle>
            <StyledLearnText>
              {t("learning.situation.text.university")}
            </StyledLearnText>
          </StyledLearnContent>
        </StyledLearnMainBox>
      </StyledLearnContainer>
      {isOpenModal === true && (
        <ChatbotModal
          isDetailOpen={isOpenModal}
          closeModal={closeModal}
          openPage={openChat}
          modalTitle={t("learning.direct.topic")}
          modalText={t("learning.direct.input")}
          imgsrc="/learning/aibody.png"
          // handleSubmit={handleSubmit}
        />
      )}
    </StyledLearnLife>
  );
};

export default LearningLife;
