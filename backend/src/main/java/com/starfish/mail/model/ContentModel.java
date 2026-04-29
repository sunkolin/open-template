package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * ContentModel
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2018-05-23
 */
@Data
public class ContentModel implements Serializable {

    private String title;

    private String content;

}
