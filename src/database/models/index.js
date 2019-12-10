import mongoose from 'mongoose';
import { config } from 'dotenv';
import User from './user';
import Contact from './contact';
import Message from './message';

config();

const connect = () => mongoose.connect(process.env.DATABASE_URL, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

mongoose.set('useCreateIndex', true);

const db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));

const models = { User, Contact, Message };

export { connect };

export default models;
