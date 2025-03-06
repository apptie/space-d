package com.dnd.spaced.core.image.application.exception;

import com.dnd.spaced.global.exception.base.ImageServerException;
import com.dnd.spaced.global.exception.code.ImageErrorCode;

public class ImageFileNotFoundException extends ImageServerException {

    public ImageFileNotFoundException(String message) {
        super(ImageErrorCode.IMAGE_FILE_NOT_FOUND_EXCEPTION, message);
    }
}
