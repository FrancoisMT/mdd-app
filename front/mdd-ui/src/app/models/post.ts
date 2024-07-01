import { Topic } from "./topic";

export class Post {
    id!: number;
    title!: string;
    description!:string;
    date!: string;
    user!:UserInfo;
    topic!: Topic;
    comments!: [];
}


export class UserInfo {
    id!:number;
    username!:string;
}

export class PostRequest {
    title!: string;
    description!:string;
    topicId!:number;
}

export class PostResponse {
    id!:number;
    title!: string;
    description!:string;
    userId!:number;
    topicId!:number;
}

export class PostDetail {
    id!:number;
    title!: string;
    description!:string;
    date!: string;
    user!: UserInfo;
    topic !: Topic;
    comments!: Comment[]
}
