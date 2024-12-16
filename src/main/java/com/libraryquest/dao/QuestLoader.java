package com.libraryquest.dao;

import com.libraryquest.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Класс для загрузки квестов из базы данных или файлов
 */
public class QuestLoader {

    private static final Logger logger = LogManager.getLogger(QuestLoader.class);

    /**
     * Получение квеста по ID
     */
    public static Quest getQuestById(int questId) {
        logger.debug("Получение квеста с ID: {}", questId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Quest quest = session.get(Quest.class, questId);
            if (quest != null) {
                logger.info("Квест с ID {} успешно загружен", questId);
            } else {
                logger.warn("Квест с ID {} не найден", questId);
            }
            return quest;
        } catch (Exception e) {
            logger.error("Ошибка при получении квеста с ID {}", questId, e);
            return null;
        }
    }

    /**
     * Получение шага по ID
     */
    public static Step getStepById(int stepId) {
        logger.debug("Получение шага с ID: {}", stepId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Step step = session.get(Step.class, stepId);
            if (step != null) {
                logger.info("Шаг с ID {} успешно загружен", stepId);
            } else {
                logger.warn("Шаг с ID {} не найден", stepId);
            }
            return step;
        } catch (Exception e) {
            logger.error("Ошибка при получении шага с ID {}", stepId, e);
            return null;
        }
    }

    /**
     * Получение всех квестов
     */
    public static List<Quest> getAllQuests() {
        logger.debug("Получение всех квестов");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Quest> quests = session.createQuery("FROM Quest", Quest.class).list();
            logger.info("Успешно загружено {} квестов", quests.size());
            return quests;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех квестов", e);
            return List.of();
        }
    }

    public static List<Score> getAllScores() {
        logger.debug("Получение всех квестов");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Score> scores = session.createQuery("FROM Score", Score.class).list();
            logger.info("Успешно загружено {} счетов", scores.size());
            return scores;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех счетов", e);
            return List.of();
        }
    }

    /**
     * Получение конкретного шага по ID квеста и ID шага
     */
    public static Step getStepContent(int questId, int stepId) {
        logger.debug("Получение шага с ID {} из квеста с ID {}", stepId, questId);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Quest quest = getQuestById(questId);
            if (quest != null) {
                Step step = quest.getSteps().stream()
                        .filter(s -> s.getStepId() == stepId)
                        .findFirst()
                        .orElse(null);
                if (step != null) {
                    logger.info("Шаг с ID {} из квеста с ID {} успешно загружен", stepId, questId);
                } else {
                    logger.warn("Шаг с ID {} в квесте с ID {} не найден", stepId, questId);
                }
                transaction.commit();
                return step;
            } else {
                logger.warn("Квест с ID {} не найден при попытке загрузить шаг с ID {}", questId, stepId);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при получении шага с ID {} из квеста с ID {}", stepId, questId, e);
        }

        return null;
    }

    /**
     * Сохранение нового квеста или обновление существующего
     */
    public static void saveQuest(Quest quest) {
        logger.debug("Сохранение квеста с ID {}", quest.getQuestId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (Step step : quest.getSteps()) {
                step.setQuest(quest); // Установите обратную связь
            }
            session.saveOrUpdate(quest); // Hibernate сам обновит связанные шаги
            transaction.commit();
            logger.info("Квест с ID {} успешно сохранен", quest.getQuestId());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при сохранении квеста с ID {}", quest.getQuestId(), e);
        }
    }

    public static void updateStep(Step step) {
        logger.debug("Обновление шага с ID {}", step.getStepId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Step existingStep = session.get(Step.class, step.getStepId());
            if (existingStep != null) {
                existingStep.setQuestion(step.getQuestion());
                existingStep.setOptions(step.getOptions());
                session.merge(existingStep);
                logger.info("Шаг с ID {} успешно обновлен", step.getStepId());
            } else {
                logger.warn("Шаг с ID {} не найден для обновления", step.getStepId());
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при обновлении шага с ID {}", step.getStepId(), e);
        }
    }

    /**
     * Удаление квеста по ID
     */
    public static void deleteQuest(Quest questDelete) {
        logger.debug("Удаление квеста с ID {}", questDelete.getQuestId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Quest quest = session.get(Quest.class, questDelete.getQuestId());
            if (quest != null) {
                session.remove(quest);
                logger.info("Квест с ID {} успешно удален", questDelete.getQuestId());
            } else {
                logger.warn("Квест с ID {} не найден для удаления", questDelete.getQuestId());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при удалении квеста с ID {}", questDelete.getQuestId(), e);
        }
    }

    public static void deleteStepById(int questId, int stepId) {
        logger.debug("Удаление шага с ID {} из квеста с ID {}", stepId, questId);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Quest quest = session.get(Quest.class, questId);
            if (quest != null) {
                Step stepToDelete = quest.getSteps().stream()
                        .filter(step -> step.getStepId() == stepId)
                        .findFirst()
                        .orElse(null);

                if (stepToDelete != null) {
                    quest.getSteps().remove(stepToDelete);
                    session.remove(stepToDelete);
                    logger.info("Шаг с ID {} успешно удален из квеста с ID {}", stepId, questId);
                } else {
                    logger.warn("Шаг с ID {} не найден в квесте с ID {} для удаления", stepId, questId);
                }
            } else {
                logger.warn("Квест с ID {} не найден для удаления шага с ID {}", questId, stepId);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при удалении шага с ID {} из квеста с ID {}", stepId, questId, e);
        }
    }

    public static User findUserByUsername(String username) {
        logger.debug("Поиск пользователя по имени: {}", username);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Очистка кэша перед запросом
            HibernateUtil.getSessionFactory().getCache().evictAllRegions();

            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();

            if (user != null) {
                session.refresh(user); // Синхронизирует сущность с базой данных
                logger.info("Пользователь найден: {}", username);
            } else {
                logger.info("Пользователь не найден: {}", username);
            }
            return user;

        } catch (Exception e) {
            logger.error("Ошибка при поиске пользователя по имени", e);
            return null;
        }
    }

    public static void saveUser(User user) {
        logger.debug("Сохранение пользователя: {}", user.getUsername());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();
            logger.info("Пользователь успешно сохранен: {}", user.getUsername());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при сохранении пользователя", e);
        }
    }

    /**
     * Обновление счёта (победы или поражения) для пользователя и квеста
     *
     * @param questId  ID квеста
     * @param username Имя пользователя
     * @param isWin    Флаг победы (true - победа, false - поражение)
     */
    public static void updateScore(int questId, String username, boolean isWin) {
        String action = isWin ? "победу" : "поражение";
        logger.debug("Обновление счёта на {} для квеста ID {} и пользователя Username {}", action, questId, username);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Поиск пользователя
            Query<User> userQuery = session.createQuery("FROM User WHERE username = :username", User.class);
            userQuery.setParameter("username", username);
            User user = userQuery.uniqueResult();
            if (user == null) {
                throw new IllegalArgumentException("Пользователь с именем " + username + " не найден.");
            }

            // Поиск квеста
            Quest quest = getQuestById(questId);
            if (quest == null) {
                throw new IllegalArgumentException("Квест с ID " + questId + " не найден.");
            }

            int userId = user.getUserId();
            String questTitle = quest.getTitle();

            // Проверка существования записи Score
            Score score = session.get(Score.class, new CompositeKey(questId, userId));
            if (score == null) {
                // Создаем новую запись
                score = new Score();
                score.setQuestId(questId);
                score.setUserId(userId);
                score.setQuestTitle(questTitle);
                score.setUserName(username);
                score.setWin(isWin ? 1 : 0);
                score.setLose(isWin ? 0 : 1);
                session.persist(score);
                logger.info("Создана новая запись счёта на {} для квеста ID {} и пользователя Username {}", action, questId, username);
            } else {
                // Обновляем существующую запись
                if (isWin) {
                    score.setWin(score.getWin() + 1);
                } else {
                    score.setLose(score.getLose() + 1);
                }
                session.merge(score); // Используем merge для создания/обновления
                logger.info("Обновлён счётчик на {} для квеста ID {} и пользователя Username {}", action, questId, username);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            logger.error("Ошибка при обновлении счёта на {} для квеста ID {} и пользователя Username {}", action, questId, username, e);
        }
    }

    /**
     * Обновление счёта в случае победы
     */
    public static void updateScoreOnWin(int questId, String username) {
        updateScore(questId, username, true);
    }

    /**
     * Обновление счёта в случае проигрыша
     */
    public static void updateScoreOnLose(int questId, String username) {
        updateScore(questId, username, false);
    }

    /**
     * Проверяет, есть ли данные в базе, и загружает начальные данные, если нужно
     */
    public static void initializeData() {
        logger.debug("Инициализация данных квестов");
        if (getAllQuests().isEmpty()) {
            logger.info("Данные отсутствуют. Загружаются данные по умолчанию.");
            loadDefaultQuests();
        } else {
            logger.info("Данные уже существуют. Инициализация не требуется.");
        }
    }

    /**
     * Загружает данные по умолчанию
     */
    private static void loadDefaultQuests() {
        logger.debug("Загрузка данных по умолчанию");
        Quest quest = new Quest();
        quest.setTitle("Красная Шапочка");
        quest.setDescription("Классический квест про девочку и волка");

        Step step1 = new Step(quest, 1, "Вы находитесь на опушке леса. Перед вами тропинка.", Map.of(
                2, "Пойти по тропинке к дому бабушки",
                4, "Сойти с тропинки и углубиться в лес"
        ));
        Step step2 = new Step(quest, 2, "Вы идете по тропинке и встречаете волка. Что вы делаете?", Map.of(
                3, "Поздороваться с волком и рассказать, куда идете",
                6, "Попытаться обойти волка незаметно"
        ));

        Step step3 = new Step(quest, 3, "Волк улыбается и спрашивает, где живет ваша бабушка. Что вы делаете?", Map.of(
                5, "Рассказать волку адрес бабушки",
                6, "Сказать, что это секрет"
        ));

        Step step4 = new Step(quest, 4, "Вы углубляетесь в лес и находите корзинку с пирожками. Что вы делаете?", Map.of(
                7, "Забрать корзинку с собой",
                8, "Оставить корзинку и вернуться на тропинку"
        ));

        // Шаги-концовки
        Step step5 = new Step(quest, 5, "Волк первым добирается к бабушке и захватывает дом! Вам нужно спасать бабушку.", null);
        Step step6 = new Step(quest, 6, "Вы не рассказываете волку, куда идете, и успеваете добраться до бабушки раньше. Вы в безопасности!", null);
        Step step7 = new Step(quest, 7, "Вы забираете корзинку. В ней оказываются волшебные пирожки, которые помогают вам быстрее дойти до бабушки.", null);
        Step step8 = new Step(quest, 8, "Вы оставляете корзинку и возвращаетесь на тропинку. Волк все равно находит вас позже.", null);

        quest.setSteps(List.of(step1, step2, step3, step4, step5, step6, step7, step8));
        saveQuest(quest);
        logger.info("Данные по умолчанию успешно загружены");
    }
}