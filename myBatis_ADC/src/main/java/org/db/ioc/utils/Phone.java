package org.db.ioc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    private int phoneId;
    private  String phoneName;
    private int personId;
}
