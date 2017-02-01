/*
 * This file is part of GameDock.
 * 
 * GameDock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GameDock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GameDock.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.gamedock.services.business.mechanics;

import io.gamedock.domain.Metric;
import io.gamedock.domain.Score;
import io.gamedock.domain.Trait;
import io.gamedock.dto.mappers.MetricMapper;
import io.gamedock.services.repositories.ScoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.gamedock.domain.Character;

@Service
public class Scoreizer {

    private final ScoreRepository scoreRepository;

    private final MetricMapper metricMapper;

    private final Scopeizer scopeizer;

    @Autowired
    public Scoreizer(ScoreRepository scoreRepository, MetricMapper metricMapper, Scopeizer scopeizer) {
        this.scoreRepository = scoreRepository;
        this.metricMapper = metricMapper;
        this.scopeizer = scopeizer;
    }

    public void generate(List<Metric> metrics, Character character) {
        List<Trait> characterTraits = character.getTraitInstances().stream().map(t -> t.getTrait()).collect(Collectors.toList());
        List<Score> scores = new ArrayList<>();
        metrics.stream().forEach(m -> {
            Set<Trait> metricTraits = m.getScope().getTraits();
            if (characterTraits.containsAll(metricTraits) && scopeizer.apply(m.getScope(), character)) {
                Score score = metricMapper.metricToScore(m);
                score.setCharacter(character);
                scores.add(score);
            }
        });
        scoreRepository.save(scores);
    }

    public void generate(Metric metric, List<Character> characters) {
        Set<Trait> metricTraits = metric.getScope().getTraits();
        List<Score> scores = new ArrayList<>();
        characters.stream().forEach(c -> {
            List<Trait> characterTraits = c.getTraitInstances().stream().map(t -> t.getTrait()).collect(Collectors.toList());
            if (characterTraits.containsAll(metricTraits) && scopeizer.apply(metric.getScope(), c)) {
                Score score = metricMapper.metricToScore(metric);
                score.setCharacter(c);
                scores.add(score);
            }
        });
        scoreRepository.save(scores);
    }

}
