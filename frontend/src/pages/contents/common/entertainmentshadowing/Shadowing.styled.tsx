import styled from "styled-components";
const StyledContentPage = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: white;
`;

const StyledButtonContainer = styled.div`
  width: 100vw;
  height: 6vh;
  position: absolute;
  bottom: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
`;

const StyledButton = styled.div.attrs<any>(() => ({}))`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 90vw;
  height: 4.5vh;
  font-family: ${(props) => props.theme.fonts.boldfont};
  font-size: ${(props) => props.theme.fontsize.large};
  border-radius: 5px;
  /* border: none; */
  border: 2px solid black;
`;
export { StyledContentPage, StyledButtonContainer, StyledButton };
