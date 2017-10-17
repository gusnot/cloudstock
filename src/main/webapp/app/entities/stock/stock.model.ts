import { BaseEntity } from './../../shared';

export class Stock implements BaseEntity {
    constructor(
        public id?: number,
        public amount?: number,
        public branchId?: number,
        public skuId?: number,
    ) {
    }
}
