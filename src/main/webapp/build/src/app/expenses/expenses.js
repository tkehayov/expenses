/**
 *  @author Tihomir Kehayov <kehayov89@gmail.com>
 */
var ang = angular.module('ngBoilerplate.expenses', [
  'ui.router',
  'placeholders',
  'ui.bootstrap'
]);

ang.config(function config($stateProvider, $httpProvider) {
  $httpProvider.interceptors.push(interceptor);
  $stateProvider.state('expense', {
    url: '/expense',
    views: {
      "main": {
        controller: 'ExpenseCtrl',
        templateUrl: 'expenses/expenses.tpl.html'
      }
    },
    data: {pageTitle: 'What is It?'}
  });
});

var interceptor = function ($q, $location) {
  return {
    // optional method
    'request': function (config) {
      // do something on success
      return config;
    },

    // optional method
    'requestError': function (rejection) {
      // do something on error
      if (canRecover(rejection)) {
        return responseOrNewPromise;
      }
      return $q.reject(rejection);
    },

    // optional method
    'response': function (response) {
      // do something on success
      return response;
    },

    // optional method
    'responseError': function (rejection) {
      // do something on error
      $scope.result = "some error occur. please try again!";

      return $q.reject(rejection);
    }
  };
};

ang.controller('ExpenseCtrl', ['$scope', '$http', '$log', function ($scope, $http, $log) {
  $scope.result = '';

  $scope.fundTypes = [
    {id: 0, name: 'college'},
    {id: 1, name: 'snack'},
    {id: 2, name: 'entertainment'}
  ];
  var selectedFundType = '';

  $scope.changed = function () {
    selectedFundType = $scope.fundType.name;
  };

  $scope.transfer = function (message) {
    message = {
      "id": "marina",
      "type": selectedFundType,
      "expenses": $scope.funds
    };


    $http.post('/expenses', message).success(function ($data, status, headers, config) {
      $scope.result = $data.message;
    }).error(function (data, status, headers, config) {
      $scope.result = "some error occur. please try again!";
    });
  };
}]);
