const minLengthValidator = (value, minLength) => {
    return value.length >= minLength;
};

const requiredValidator = value => {
    return value.trim() !== '';
};

const ageValidator = (value, min, max) => {
    return value >= min && value <= max;
};

const validate = (value, rules) => {
    let isValid = true;

    for (let rule in rules) {
        switch (rule) {
            case 'minLength':
                isValid = isValid && minLengthValidator(value, rules[rule]);
                break;

            case 'isRequired':
                isValid = isValid && requiredValidator(value);
                break;

            case 'ageLimit':
                isValid = isValid && ageValidator(value, rules[rule].min, rules[rule].max);
                break;

            default:
                isValid = true;
        }
    }

    return isValid;
};

export default validate;
