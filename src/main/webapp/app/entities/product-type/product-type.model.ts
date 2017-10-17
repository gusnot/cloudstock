import { BaseEntity } from './../../shared';

export class ProductType implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public prefix?: string,
    ) {
    }
}
