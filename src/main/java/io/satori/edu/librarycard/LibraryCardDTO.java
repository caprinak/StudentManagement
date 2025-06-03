package io.satori.edu.librarycard;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LibraryCardDTO {

    private Integer id;

    @NotBlank(message = "Library card number is required")
    @Size(min = 4, max = 20, message = "Card number must be between 4 and 20 characters")
    @Pattern(regexp = "^[0-9]{4,}$", message = "Card number must contain at least 4 digits")
    private String cardNumber;

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    public LibraryCardDTO() {
    }

    public LibraryCardDTO(String cardNumber, Integer studentId) {
        this.cardNumber = cardNumber;
        this.studentId = studentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
