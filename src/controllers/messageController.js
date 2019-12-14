import Model from '../database/models';

const { Message } = Model;

const getMessages = async (req, res) => {
  try {
    const messages = await Message.find({}).populate('user').populate('contact');
    res.json({
      success: true,
      messages,
    });
  } catch (error) {
    console.log(error);
  }
};

export default getMessages;
