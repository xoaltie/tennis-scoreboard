package com.github.xoaltie.tennisscoreboard.service.result;

import java.util.List;

public record MatchPageResult(List<MatchResult> matchResults, int currentPage, int totalPages) {
}
