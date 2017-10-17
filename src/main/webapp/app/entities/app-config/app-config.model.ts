import { BaseEntity } from './../../shared';

export class AppConfig implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: string,
    ) {
    }
}
