vireo.filter('withouutId', function() {
    return function(initialValues, withoutId) {
        var newValues = [];
        angular.forEach(initialValues, function(value) {
            if (value.id !== withoutId) {
                newValues.push(value);
            }
        });
        return newValues;
    };
});
