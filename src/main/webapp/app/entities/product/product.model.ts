import { BaseEntity } from './../../shared';

export class Product implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public productTypeId?: number,
        public attributes?: BaseEntity[],
    ) {
    }
}
