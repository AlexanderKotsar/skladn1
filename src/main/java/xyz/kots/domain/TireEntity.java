package xyz.kots.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by kots on 23.01.2018.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TireEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String tireType;

    @Column
    private String brandName;

    @Column
    private String modelName;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private String diameter;

    @Column
    private String season;

    @Column
    private String loadIndex;

    @Column
    private String speedIndex;

    @Column
    private String reinforced;

    @Column
    private String spikeNonShip;

    @Column
    private String extrasOptions;

    @Column
    private String country;

    @Column
    private String year;

    @Column
    private int balance;

    @Column
    private int price;
}
