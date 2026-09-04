document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("noteForm");
    if (!form) return;

    form.addEventListener("submit", function (e) {
        var content = form.querySelector("[name='content']");
        var author = form.querySelector("[name='author']");
        var msg = document.getElementById("clientValidationMsg");
        var errors = [];

        if (content && /[<>]/.test(content.value)) {
            errors.push("Note content cannot contain '<' or '>' characters.");
        }
        if (content && content.value.length > 2000) {
            errors.push("Note content must be under 2000 characters.");
        }
        if (author && !/^[\p{L}0-9 .'-]+$/u.test(author.value)) {
            errors.push("Author name contains invalid characters.");
        }

        if (errors.length > 0) {
            e.preventDefault();
            msg.textContent = errors.join(" ");
            msg.style.display = "block";
        } else if (msg) {
            msg.style.display = "none";
        }
    });
});
