DELIMITER $$

DROP PROCEDURE IF EXISTS sp_search_members_by_name$$

CREATE PROCEDURE sp_search_members_by_name(IN search_term VARCHAR(255))
BEGIN
    SELECT id, full_name, age, suburb, email, medical_note, available_slot
    FROM member
    WHERE full_name LIKE CONCAT('%', search_term, '%');
END$$

DELIMITER ;
