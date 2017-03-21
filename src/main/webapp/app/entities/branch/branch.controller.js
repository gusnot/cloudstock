(function() {
    'use strict';

    angular
        .module('cloudstockApp')
        .controller('BranchController', BranchController);

    BranchController.$inject = ['Branch'];

    function BranchController(Branch) {

        var vm = this;

        vm.branches = [];

        loadAll();

        function loadAll() {
            Branch.query(function(result) {
                vm.branches = result;
                vm.searchQuery = null;
            });
        }
    }
})();
