package com.ddoya.show.artist.dto;

import com.ddoya.show.common.dto.ShowClipDto;
import lombok.Data;

import java.util.List;

@Data
public class ArtistShowResultDto {

    int result;

    int clipCnt;

    List<ShowClipDto> clipList;
}