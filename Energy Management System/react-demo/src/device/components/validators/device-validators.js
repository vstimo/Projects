const minLengthValidator = (value, minLength) => {
    return value.length >= minLength;
};

const requiredValidator = value => {
    return value.trim() !== '';
};

const rangeValidator = (value, min) => {
    return value >= min;
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

            case 'rangeLimit':
                isValid = isValid && rangeValidator(value, rules[rule].min);
                break;

            default:
                isValid = true;
        }
    }

    return isValid;
};

export default validate;
