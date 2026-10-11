package com.example.carlos.primeiro_projeto.structured;

import java.util.List;

public record CourseSummary (String title,
                             String level,
                             List<String> learningGoals,
                             int estimatedHours){

}
