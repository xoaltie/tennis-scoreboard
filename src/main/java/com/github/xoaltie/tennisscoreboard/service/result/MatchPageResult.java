package com.github.xoaltie.tennisscoreboard.service.result;

import java.util.List;

public record MatchPageResult(List<MatchResult> results, int currentPage, int totalPages) {
}
